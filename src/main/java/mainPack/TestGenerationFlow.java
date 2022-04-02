package mainPack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.javaparser.utils.LineSeparator;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import utils.ProjectUtils;

public class TestGenerationFlow {

	public ResultObject start(String pathToProjectFolder, String pathToWorkDir) throws MavenInvocationException, SAXException, IOException, ParserConfigurationException {
		String pathToPomXML = pathToProjectFolder+File.separator+"pom.xml";
		//executing maven clean package for the cloned project
		if(!mavenCleanPackage(pathToProjectFolder, pathToPomXML)) {
			return new ResultObject(1, "Failde to execute 'mvn clean package'");
		}

		//finding the target folder of the project
		String targetFolder = ProjectUtils.findFolderPath(pathToProjectFolder, "target");
		if(targetFolder==null) {
			System.out.println("***Could not find 'target' folder in the project after maven 'clean install'");
			return new ResultObject(1, "Could not find 'target' folder in the project after maven 'clean install'");
		}
		System.out.println("project 'target' folder path: " + targetFolder);

		//copying classes (after maven build) from the target folder to a new one
		boolean result = copyFolderToDestination(targetFolder+File.separator+"classes", pathToWorkDir+File.separator+"smartCLIDE_workFolder");
		if(!result) {
			System.out.println("*** failed to copy classes");
			return new ResultObject(1, "Could not copy classes from the project's target folder");
		}

		//try and extract all the dependencies of the project, in order to generate the tests
		if(!extractProjectDependencies(pathToPomXML, pathToProjectFolder, targetFolder, pathToWorkDir)) {
			System.out.println("failed to extract the project's dependencies");
		}

		//create class file list
		try {
			String temp = createClassListFile(targetFolder+File.separator+"classes", pathToWorkDir, "classList_randoop.txt");
			System.out.println("Path to list: "+temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block

			System.out.println("*** failed to create class list");
			e.printStackTrace();
		}

		//copy randoop jar to workfolder
		String pathToRandoopJar = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"randoop-all-4.3.0.jar";
		Files.copy(new File(pathToRandoopJar).toPath(), new File(pathToWorkDir+File.separator+"randoop-all-4.3.0.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		//run randoop jar for test generation
		JarExecutor test = new JarExecutor();
		try {
			test.execCmdCommand(pathToWorkDir, pathToWorkDir+File.separator+"smartCLIDE_workFolder;randoop-all-4.3.0.jar");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}


		return new ResultObject(0, "***ola kala!!!");
	}

	//*** finding project classfiles from targetfolder
	private String createClassListFile(String targetClassFolder, String destinationFolder, String listFileName) throws IOException {

		List<String> classFileList = new ArrayList<>();
		//System.out.println(targetClassFolder);
		getFilesFromExtension(targetClassFolder, classFileList, ".class");
		//System.out.println(classFileList.toString());

		List<String> lines = new ArrayList();

		String removeFromPath = targetClassFolder.replace(File.separator, "/") + "/";
		for(String classPath : classFileList) {
			//String temp = classPath.replaceFirst(targetClassFolder+File.separator, "");
			String temp = classPath.replace(File.separator, "/");
			temp = temp.replaceFirst(removeFromPath, "");
			temp = temp.replace(".class", "");
			temp = temp.replace("/", ".");
			lines.add(temp);
		}


		//System.out.println(lines.toString());
		//writing the classpaths to a file 
		Path file = Paths.get(destinationFolder, listFileName);
		//destinationFolder
		Files.write(file, lines, StandardCharsets.UTF_8);
		//Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND);

		return file.toAbsolutePath().toString();
	}

	private boolean extractProjectDependencies(String pathToPomXML, String pathToProjectFolder, String targetFolder, String pathToWorkDir) {
		//extracting the target .jar (after maven build) to a new folder
		String extractFolder = extractPackagedJarToFolder(pathToPomXML, pathToProjectFolder, targetFolder, pathToWorkDir);
		if(extractFolder==null) {
			return false;
		}

		//move jar's org folder to smartCLIDE_workFolder
		String jarOrgFolder = ProjectUtils.findFolderPath(extractFolder, "org");
		copyFolderToDestination( jarOrgFolder, pathToWorkDir+File.separator+"smartCLIDE_workFolder"+File.separator+"org");

		//find all BOOT-INF/lib/*.jars
		String libFolder = ProjectUtils.findFolderPath(extractFolder, "BOOT-INF");
		libFolder = ProjectUtils.findFolderPath(libFolder, "lib");
		System.out.println(libFolder);

		//find all .jar files in lib folder
		List<String> jarFilePaths = new ArrayList<>();
		getFilesFromExtension(libFolder, jarFilePaths, ".jar");

		//extract all .jar files from lib folder
		jarFilePaths.parallelStream().forEach(jarPath ->
		unzipPackagedJar(pathToWorkDir+File.separator+"smartCLIDE_workFolder", jarPath)
				);

		return true;
	}

	private String extractPackagedJarToFolder(String pathToPomXML, String pathToProjectFolder, String targetFolder, String pathToWorkDir) {
		String jarPackageName;
		try {
			jarPackageName = getJarPackageNameFromPom(pathToPomXML);
			System.out.println("jarPackageName: "+jarPackageName);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not find maven packaged jar name from pom.xml");
			e.printStackTrace();
			return null;
		}
		String filePath = ProjectUtils.findFilePath(targetFolder, jarPackageName);
		if(filePath==null) {
			System.out.println("Could not find '"+jarPackageName+"' file in folder "+targetFolder);
			return null;
		}
		System.out.println("filePath: "+filePath);
		return unzipPackagedJar(pathToWorkDir+File.separator+"smartCLIDE_extractedJar", filePath);
	}

	private boolean mavenCleanPackage(String pathToProject, String pathToPomXML) throws MavenInvocationException {
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( new File( pathToPomXML ) );
		request.setGoals( Arrays.asList( "clean", "package" ) );

		Invoker invoker = new DefaultInvoker();
		invoker.setLocalRepositoryDirectory(new File(pathToProject));
		invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
		InvocationResult invRes = invoker.execute( request );

		if(invRes.getExitCode()!=0) {
			System.out.println("Maven clean package result:\n "+invRes.getExecutionException().getMessage());
			return false;
		}

		return true;
	}

	private boolean copyFolderToDestination(String sourceFolder, String destinationFolder) {
		File srcDir = new File(sourceFolder);
		File destDir = new File(destinationFolder);
		try {
			FileUtils.copyDirectory(srcDir, destDir);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	private void getFilesFromExtension(String path, List<String> fileList, String fileExtension) {
		File[] allFiles = new File(path).listFiles();

		if (allFiles != null) {
			for (File file : allFiles) {
				if (file.isFile() && file.getName().endsWith(fileExtension)) {
					fileList.add(file.getAbsolutePath());
				} else if (file.isDirectory()) {
					getFilesFromExtension(file.getAbsolutePath(), fileList, fileExtension);
				}
			}
		}
	}

	private String unzipPackagedJar(String destinationFolder, String pathToJar){
		System.out.println(pathToJar);
		try {
			ZipFile zipFile = new ZipFile(pathToJar);
			//String destinationFolder = pathToWorkDir+File.separator+"smartCLIDE_extractedJar";
			System.out.println("extracting jar to : "+destinationFolder);
			zipFile.extractAll(destinationFolder);
			System.out.println("Unzip executed successfully");
			return destinationFolder;
		} catch (ZipException e) {
			System.out.println("Unzip failed");
			System.out.println(e.getMessage());
			return null;
		}

	}

	private String getJarPackageNameFromPom(String pathtToPomXML) throws SAXException, IOException, ParserConfigurationException {
		String ret = null;

		File file = new File(pathtToPomXML);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(file);

		NodeList pomProjectElements = document.getElementsByTagName("project");

		if(pomProjectElements.getLength()!=1) {
			System.out.println("***too many element woth the given name...");
			return null;
		}

		Node projectNode = pomProjectElements.item(0);
		NodeList childNodes = projectNode.getChildNodes();
		String artifactID = null;
		String version = null;
		for (int i=0; i<childNodes.getLength(); i++) {
			if(artifactID!=null && version!=null ) {
				break;
			}
			switch (childNodes.item(i).getNodeName()) {
			case "artifactId":
				artifactID = childNodes.item(i).getTextContent();
				continue;
			case "version":
				version = childNodes.item(i).getTextContent();
				continue;
			default:
				continue;
			}

		}

		if(artifactID!=null && version!=null ) {
			ret = artifactID+"-"+version+".jar";
		}

		return ret;
	}
}

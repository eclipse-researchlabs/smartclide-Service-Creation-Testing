package mainPack;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import utils.ProjectUtils;

public class MainFlow {

	Git gitProject;
	String srcPath;
	String projectClonePath;

	public ResultObject start(String repoUrl) {
	
		//creating work folder
		String folder_name = repoUrl.replaceAll("[^A-Za-z0-9]","");
		folder_name += "_workdir";
		String workDirPath = createFolder(folder_name);
		if(workDirPath==null) {
			return new ResultObject(1, "Could not create a folder inside the project");
		}
		
		if(!cloneProject(repoUrl)) {
			return new ResultObject(1, "Could not clone project from the given URL: "+repoUrl);
		}
		System.out.println("Project root folder path: "+this.projectClonePath);
		
		this.srcPath = ProjectUtils.findFolderPath(this.projectClonePath, "src");
		if(this.srcPath==null) {
			return new ResultObject(1, "Could not find 'src' folder in the project from the given URL: "+repoUrl);
		}
		System.out.println("Project source folder path: "+this.srcPath);
		
		//process for the generation of the tests
		TestGenerationFlow genFLow = new TestGenerationFlow();
		try {
			genFLow.start(this.projectClonePath, workDirPath);
			System.out.println("maven flow done....");
		} catch (Exception e) {
			System.out.println("exception during maven flow....");
			e.printStackTrace();
		}
		
		//clean project
		//copy generated tests into project
		//commit changes
		
		//-----------------------------------------------------------------------------------------------------------
		gitProject.getRepository().close();
		deleteDir(new File(projectClonePath));
		deleteDir(new File(workDirPath));
		System.out.println("Ola kala!!!");
		return new ResultObject(0, "Ola kala");
	}

	
	private String createFolder(String folderName) {
		File newFolder = new File(folderName);
		if (!newFolder.mkdir()) {
			return null;
		}
		return newFolder.getAbsolutePath();
	}
	
	private boolean cloneProject(String repoUrl) {
		//new directory creation
		String folder_name = repoUrl.replaceAll("[^A-Za-z0-9]","");
		folder_name += "_cloned";
		String cloneDirectoryPath = createFolder(folder_name);
		boolean flag = (cloneDirectoryPath!=null);
		if(!flag) {
			System.out.println("could not create folder");
			return false;
		}

		System.out.println("Clone path: "+cloneDirectoryPath);
		this.projectClonePath = cloneDirectoryPath;
		try {
			System.out.println("Cloning "+repoUrl+" into "+cloneDirectoryPath);
			this.gitProject = Git.cloneRepository()
					.setURI(repoUrl)
					.setDirectory(Paths.get(cloneDirectoryPath).toFile())
					.call();
			System.out.println("Completed Cloning");
			return true;
		} catch (GitAPIException e) {
			System.out.println("Exception occurred while cloning repo");
			deleteDir(new File(cloneDirectoryPath));
			e.printStackTrace();
			return false;
		}

	}

	

	public void fetchMethods(String sourceFilePath) {
		MethodFinder mdfndr = new MethodFinder();
		mdfndr.find(sourceFilePath);
	}

	private void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (! Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}

		String delFolderName = file.getName();
		boolean isDir = file.isDirectory();
		if(!file.delete()) {
			System.out.println("Could not delete folder: "+delFolderName);
			System.out.println("is Directory: "+isDir);
		}

	}


















	//---------------------------------------------------------------------------------------------------------------------------

	//	public void prevMain(String[] args) {
	//
	//		ObjectMapper mapper = new ObjectMapper();
	//		try {
	//			JsonSchema shcema = mapper.generateJsonSchema(TempClass.class);
	//			System.out.println(shcema.toString());
	//		} catch (JsonMappingException e) {
	//			// TODO Auto-generated catch block
	//			System.out.println("Error catch...");
	//			e.printStackTrace();
	//		}
	//
	//	}
	//	
	//	public String getTestMethod() {
	//		String ret = "";
	//
	//		RestTemplate rest = new RestTemplate();
	//		ret = rest.getForObject("http://195.251.210.147:3997/GetRepoURL", String.class);
	//		//String result = rest.postForObject("http://195.251.210.147:3997", String.class);
	//		System.out.println(ret);
	//
	//		//ObjectMapper mapper = new ObjectMapper();
	//		//Object object = mapper.readValue(result, ExampleJson.class);
	//
	//		return ret;
	//	}


}

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

public class MainFlow {

	Git gitProject;
	String srcPath;
	String cloneFolderName;
	String projectClonePath;

	public ResultObject start(String repoUrl) {

		if(!cloneProject(repoUrl)) {
			return new ResultObject(1, "Could not clone project from the given URL: "+repoUrl);
		}
		System.out.println("Project root folder path: "+this.projectClonePath);

		this.srcPath = findFolder(this.projectClonePath, "src");
		if(this.srcPath==null) {
			return new ResultObject(1, "Could not find 'src' folder in the project from the given URL: "+repoUrl);
		}
		System.out.println("Project source folder path: "+this.srcPath);
		
		TestGenerationFlow genFLow = new TestGenerationFlow();
		try {
			genFLow.start(this.projectClonePath, this.projectClonePath+File.separator+"pom.xml");
			System.out.println("maven flow done....");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception during maven flow....");
			e.printStackTrace();
		}
		
		
		//-----------------------------------------------------------------------------------------------------------
		gitProject.getRepository().close();
		File clone_folder = new File(this.cloneFolderName);
		deleteDir(clone_folder);
		System.out.println("Ola kala!!!");
		return new ResultObject(0, "Ola kala");
	}

	private boolean cloneProject(String repoUrl) {
		//new directory creation
		String folder_name = repoUrl.replaceAll("[^A-Za-z0-9]","");
		folder_name += "_cloned";

		System.out.println("Creating new folder...");
		this.cloneFolderName = folder_name;
		File clone_folder = new File(folder_name);
		boolean flag = clone_folder.mkdir();

		if(!flag) {
			System.out.println("could not create folder");
			return false;
		}

		String cloneDirectoryPath = clone_folder.getAbsolutePath(); // Ex.in windows c:\\gitProjects\SpringBootMongoDbCRUD\
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
			deleteDir(clone_folder);
			e.printStackTrace();
			return false;
		}

	}

	private String findFolder(String statingFolderPath, String folderName) {

		File clone_folder = new File(statingFolderPath);
		
		return findFolderByNamePerLevel(clone_folder, folderName);
	}
	
	private String findFolderByNamePerLevel(File root, String searchName) {
		String folderPath = root.getAbsolutePath();
		List<String> nextLevel = new ArrayList<String>();
		String[] fileNames = getDirectories(root);
		Arrays.asList(fileNames).forEach(fileName -> {
			nextLevel.add(folderPath+File.separator+fileName);
	    });
		
		return findFolderByNamePerLevelRecurse(nextLevel, searchName);

	}
	
	private String findFolderByNamePerLevelRecurse(List<String> folders, String searchName) {
		
		List<String> nextLevel = new ArrayList<String>();
		
		for(String folderPath : folders) {
			System.out.println("checking : "+folderPath);
			File root = new File(folderPath);
			if (root.getName().equals(searchName)) {
				return root.getAbsolutePath();
			}
			
			String[] fileNames = getDirectories(root);
			if(fileNames==null || fileNames.length==0) {
				continue;
			}
			Arrays.asList(fileNames).forEach(fileName -> {
				nextLevel.add(folderPath+File.separator+fileName);
		    });
		}

		if(nextLevel.size()!=0) {
			System.out.println("To next level");
			return findFolderByNamePerLevelRecurse(nextLevel, searchName);
		}
		
		return null;
	}
	
	private String[] getDirectories(File folder) {
		String[] directories = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return directories;
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

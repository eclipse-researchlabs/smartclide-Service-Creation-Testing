package uom.smartclide.testgeneration.functionality.mainFlow;

/*
 * Copyright (C) 2021 UoM - University of Macedonia
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 */



import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import uom.smartclide.testgeneration.functionality.utils.ProjectUtils;
import uom.smartclide.testgeneration.functionality.utils.ResultObject;

public class MainFlow {

	Git gitProject;
	String srcPath;
	String projectClonePath;
	String workDirPath;

	public ResultObject start(String repoUrl, String username, String password) {
		ResultObject ret = startFlow(repoUrl, username, password);
		try {	
			gitProject.getRepository().close();
			System.out.println("Ola kala!!!");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			System.out.println(workDirPath);
			System.out.println(new File(workDirPath).getAbsolutePath());
			deleteDir(new File(projectClonePath));
			deleteDir(new File(workDirPath));
			System.out.println("main flow executed correctly");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

		return ret;
	}

	private ResultObject startFlow(String repoUrl, String username, String password) {

		//creating work folder
		String folder_name = repoUrl.replaceAll("[^A-Za-z0-9]","");
		folder_name += "_workdir";
		String workDirPath = createFolder(folder_name);
		if(workDirPath==null) {
			return new ResultObject(1, "Could not create a folder inside the project");
		}
		this.workDirPath = workDirPath;

		if(!cloneProject(repoUrl, username, password)) {
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
		ResultObject testGen = genFLow.start(this.projectClonePath, workDirPath);
		if(testGen.getStatus()!=0) {
			System.out.println("TestGenerationFlow failed");
			return testGen;
		}
		System.out.println("maven flow done....");

		//reset project
		System.out.println("Attempting Repo Reset and Clean");
		try {
			gitProject.reset().setMode(ResetType.HARD).call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return new ResultObject(1, "Could not reset the project from the given URL: "+repoUrl);
			
		}
		try {
			gitProject.clean().setForce(true).setCleanDirectories(true).call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return new ResultObject(1, "Could not clean the project from the given URL: "+repoUrl);
		}

		//copy generated tests into project src/test/java (Maven Standard Directory Layout)
		try {
			ProjectUtils.copyFolderToDestination(workDirPath+File.separator+"smartCLIDE_tests", this.srcPath+File.separator+"test"+File.separator+"java"+File.separator+"smartCLIDE_tests");
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return new ResultObject(1, "Could not find smartCLIDE_tests folder, because the test generation failed");
		}
		
		//commit changes
		if(!commitPushTestsToRepo(username, password)) {
			return new ResultObject(1, "Could not commit generated tests to the project from the given URL: "+repoUrl);
		}

		return new ResultObject(0, "Ola kala");
	}

	private boolean commitPushTestsToRepo(String username, String password) {
		try {
			this.gitProject.add().addFilepattern(".").call();
			this.gitProject.commit().setMessage("SMartCLIDE - autogenerated tests").call();

			CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
			this.gitProject.push().setCredentialsProvider(cp).call();

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private String createFolder(String folderName) {
		File newFolder = new File(folderName);
		if (!newFolder.mkdir()) {
			return null;
		}
		return newFolder.getAbsolutePath();
	}

	private boolean cloneProject(String repoUrl, String username, String password) {
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
			//cloning a public repo, without authentication
			//			this.gitProject = Git.cloneRepository()
			//					.setURI(repoUrl)
			//					.setDirectory(Paths.get(cloneDirectoryPath).toFile())
			//					.call();

			CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
			this.gitProject = Git.cloneRepository()
					.setURI(repoUrl)
					.setCredentialsProvider(cp)
					.setDirectory(Paths.get(cloneDirectoryPath).toFile())
					.call();

			System.out.println("Completed Cloning");
			return true;
		} catch (GitAPIException e) {
			System.out.println("Exception occurred while cloning repo");
			//deleteDir(new File(cloneDirectoryPath));
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}

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

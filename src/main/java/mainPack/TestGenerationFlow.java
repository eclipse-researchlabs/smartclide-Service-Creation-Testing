package mainPack;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import utils.ProjectUtils;

public class TestGenerationFlow {

	
	public ResultObject start(String pathToProjectFolder, String pathToPomXML) throws MavenInvocationException {
		
		//ResultObject mavenCmd = mavenCleanInstall(pathToProjectFolder, pathToPomXML);
		
		
		String targetFolder = ProjectUtils.findFolderPath(pathToProjectFolder, "target");
		if(targetFolder==null) {
			System.out.println("***Could not find 'target' folder in the project after maven 'clean install'");
			return new ResultObject(1, "Could not find 'target' folder in the project after maven 'clean install'");
		}
		System.out.println("project 'target' folder path: " + targetFolder);
		
		String tempfilename= "smartclide-service-creation-0.0.1-SNAPSHOT.jar";
		String filePath = ProjectUtils.findFilePath(targetFolder, tempfilename);
		if(targetFolder==null) {
			System.out.println("Could not find 'target' folder in the project after maven 'clean install'");
			return new ResultObject(1, "Could not find 'target' folder in the project after maven 'clean install'");
		}
		
		return new ResultObject(0, "***ola kala!!!");
	}
	
	private ResultObject mavenCleanInstall(String pathToProject, String pathToPomXML) throws MavenInvocationException {
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( new File( pathToPomXML ) );
		request.setGoals( Arrays.asList( "clean", "install" ) );

		Invoker invoker = new DefaultInvoker();
		invoker.setLocalRepositoryDirectory(new File(pathToProject));
		invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
		InvocationResult invRes = invoker.execute( request );

		if(invRes.getExitCode()!=0) {
			return new ResultObject(1, invRes.getExecutionException().getMessage());
		}
		
		return new ResultObject(0, "maven clean install completed");
	}
}

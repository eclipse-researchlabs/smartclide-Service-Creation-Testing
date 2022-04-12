package uom.smartclide.testgeneration.functionality.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class JarExecutor {

	private BufferedReader error;
	private BufferedReader op;
	private int exitVal;

	public void execCmdCommand(String workDir, String classPath) throws IOException, InterruptedException {
		System.out.println("executing randoop.....");

//		System.out.println("workDir: "+workDir);
//		System.out.println("classList_randoop.txt exists?: "+new File(workDir+File.separator+"classList_randoop.txt").exists());
//		System.out.println("classPath: "+classPath);
//		debugUnixCommands(workDir);
		
		ProcessBuilder processBuilder = new ProcessBuilder( "java"
				, "-cp"
				, classPath
				, "randoop.main.Main"
				, "gentests"
				,"--classlist=classList_randoop.txt"
				,"--attempted-limit=100"
				,"--output-limit=100"
				,"--junit-package-name=smartCLIDE_tests");

		processBuilder.directory(new File(workDir));
		Process p = processBuilder.start();

		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		System.out.println();
		while ((line = reader.readLine()) != null)
			System.out.println(line);
		int exitValue = process.waitFor();



		if(exitValue==0) {
			System.out.println("done executing!");
		}else {
			System.out.println("execution failed!");
		}
	}

	private void debugUnixCommands(String workDir) throws IOException, InterruptedException {

		
		System.out.println("------------***ls<");
		ProcessBuilder processBuilder = new ProcessBuilder( "ls"
				, "-aR");
		System.out.println("------------***ls>");

		processBuilder.directory(new File(workDir));
		Process p = processBuilder.start();

		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		System.out.println();
		while ((line = reader.readLine()) != null)
			System.out.println(line);

	}
}

package mainPack;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;

//TODO
public class JarExecutor {

	private BufferedReader error;
	private BufferedReader op;
	private int exitVal;

	/*
	 * java 
	 * -classpath 
	 * 'classesjar;classesjar\BOOT-INF\classes;installation\randoop-all-4.3.0.jar' 
	 * randoop.main.Main 
	 * gentests 
	 * --classlist=list.txt 
	 * --attempted-limit=100 
	 * --output-limit=100 
	 * --junit-package-name=testAll
	 */
	
	public void execCmdCommand(String workDir, String classPath) throws IOException, InterruptedException {
		System.out.println("executing randoop.....");
		
		ProcessBuilder processBuilder = new ProcessBuilder( "java"
				, "-classpath"
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
	
}

package mainPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MainClass {
	
	public static void main (String[] args) {
		String sourceFilePath = "main/java/uom/smartclide/structurecreation/controller/EndpointController.java";
		String repoURL = "https://github.com/eclipse-researchlabs/smartclide-service-creation";
		String projectCloneFolder = "C:\\Users\\Maik\\Documents\\GitHub\\unit_test_generation\\httpsgithubcomeclipseresearchlabssmartclideservicecreation_cloned";
		
		System.out.println("Start");
		MainFlow test = new MainFlow();
		test.start(repoURL);
		System.out.println("End");
		/////test.fetchMethods(sourceFilePath);
		
		
		//**************************
		
//		TestGenerationFlow test = new TestGenerationFlow();
//		try {
//			test.start(projectCloneFolder);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		TestGenerationFlow test = new TestGenerationFlow();
//		test.test();
		
//		String workDir = "C:\\Users\\Maik\\Documents\\GitHub\\unit_test_generation\\httpsgithubcomeclipseresearchlabssmartclideservicecreation_workdir";
//		String classPath = "smartCLIDE_workFolder;randoop-all-4.3.0.jar";
//		JarExecutor test = new JarExecutor();
//		try {
//			test.execCmdCommand(workDir, classPath);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		
	}
	

	
}

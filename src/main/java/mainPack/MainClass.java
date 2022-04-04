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

import utils.ProjectUtils;

public class MainClass {
	
	public static void main (String[] args) {
//		String repoURL = "https://github.com/eclipse-researchlabs/smartclide-service-creation";
//		//String repoURL = "https://gitlab.dev.smartclide.eu/teomaik19/smartclide-service-creation";
//		
//		String sourceFilePath = "main/java/uom/smartclide/structurecreation/controller/EndpointController.java";
//		String projectCloneFolder = "C:\\Users\\Maik\\Documents\\GitHub\\unit_test_generation\\httpsgithubcomeclipseresearchlabssmartclideservicecreation_cloned";
//		
//		System.out.println("Start");
//		MainFlow test = new MainFlow();
//		test.start(repoURL);
//		System.out.println("End");
//		/////test.fetchMethods(sourceFilePath);
		
		
		
		//**************************************************************************************************************************************************
		
		String workDirPath = "C:\\Users\\Maik\\Documents\\GitHub\\unit_test_generation\\httpsgithubcomeclipseresearchlabssmartclideservicecreation_workdir";
		String srcPath = "C:\\Users\\Maik\\Documents\\GitHub\\unit_test_generation\\httpsgithubcomeclipseresearchlabssmartclideservicecreation_cloned\\src";
		ProjectUtils.copyFolderToDestination(workDirPath+File.separator+"smartCLIDE_tests", srcPath+File.separator+"test"+File.separator+"java"+File.separator+"smartCLIDE_tests");
		

		

		
	}
	

	
}

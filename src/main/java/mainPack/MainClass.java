package mainPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
		
		MainFlow test = new MainFlow();
		test.start(repoURL);
		//test.fetchMethods(sourceFilePath);
	}
	

	
}

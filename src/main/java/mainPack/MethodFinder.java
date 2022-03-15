package mainPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodFinder {

	public void find(String sourceFilePath) {
		
		File f = new File(sourceFilePath);
		try {
			List<String> methods = new ArrayList<String>();
			
			CompilationUnit cu = StaticJavaParser.parse(f);
			VoidVisitor<List<String>> methodNameVisitor = new MethodNamePrinter();
			methodNameVisitor.visit(cu, methods);
			
			methods.forEach(n-> System.out.println(n));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class MethodNamePrinter extends VoidVisitorAdapter<List<String>> {
		@Override
		public void visit(MethodDeclaration md, List<String> collector) {
			super.visit(md, collector);
			for(AnnotationExpr n: md.getAnnotations()){
                if(n.getName().asString().contains("Mapping")){
                    for(Node node: n.getChildNodes()){
                        if(node.toString().contains("path = ")){
                           System.out.println(node.toString());
                        }
                    }
                }
            }
			if(!md.getAnnotationByName("GetMapping").isEmpty()) {
				//System.out.println(md.getAnnotation(0).getChildNodes().get(1));
				collector.add(md.getDeclarationAsString() +";"+ md.getBegin().toString().replaceAll(",(.*)", "").replaceAll("(.*)(line )", "") +";"+
																			md.getEnd().toString().replaceAll(",(.*)", "").replaceAll("(.*)(line )", ""));
			}
			if(!md.getAnnotationByName("PostMapping").isEmpty()) {
				//System.out.println(md.getAnnotation(0).getChildNodes().get(1));
				collector.add(md.getDeclarationAsString() +";"+ md.getBegin().toString().replaceAll(",(.*)", "").replaceAll("(.*)(line )", "") +";"+
																			md.getEnd().toString().replaceAll(",(.*)", "").replaceAll("(.*)(line )", ""));
			}
		}
	}
}

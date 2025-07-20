import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.body.Parameter;

import java.io.File;

public class ApiMetadataExtractor {
    public static void main(String[] args) throws Exception {
        // Load your Java source file (change the path as needed)
        File file = new File("ApiController.java");
        CompilationUnit cu = StaticJavaParser.parse(file);

        // Loop through all methods
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            System.out.println("\u25B6 Method: " + method.getName());
            System.out.println("   Return Type: " + method.getType());

            // Method-level annotations (e.g. @GetMapping)
            for (AnnotationExpr annotation : method.getAnnotations()) {
                System.out.println("   Annotation: " + annotation.getName());
                if (annotation.toString().contains("(")) {
                    String path = annotation.toString().split("\\(")[1].replace(")", "").replace("\"", "");
                    System.out.println("   Path: " + path);
                }
            }

            // Parameters
            for (Parameter param : method.getParameters()) {
                System.out.println("   Param: " + param.getName() + " (" + param.getType() + ")");
                for (AnnotationExpr paramAnn : param.getAnnotations()) {
                    System.out.println("      " + paramAnn.getName());
                }
            }

            System.out.println();
        }
    }
} 
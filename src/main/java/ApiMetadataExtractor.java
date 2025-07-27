import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;

import java.io.File;

public class ApiMetadataExtractor {
    public static void main(String[] args) throws Exception {
        // extract java source file
        File file = new File("flat_file");
        CompilationUnit cu = StaticJavaParser.parse(file);

        // Loop through all methods
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            System.out.println("\u25B6 Signature: " + method.getName());
            System.out.println("   Return Type: " + method.getType());

            // find HTTP method and path 
            for (AnnotationExpr annotation : method.getAnnotations()) {
                String annotationName = annotation.getNameAsString();

                if (annotationName.endsWith("Mapping")) {
                    // Extract and print the HTTP method 
                    String httpMethod = annotationName.replace("Mapping", "").toLowerCase();
                    System.out.println("   Method: " + httpMethod);

                    // path from annotation
                    String path = "";
                    if (annotation.isSingleMemberAnnotationExpr()) {       // eg.@GetMapping("/users")
                        path = annotation.asSingleMemberAnnotationExpr().getMemberValue().toString();
                    } else if (annotation.isNormalAnnotationExpr()) {      // eg. @G(value = etMapping"/users")
                        path = annotation.asNormalAnnotationExpr().getPairs().stream()
                            .filter(p -> p.getNameAsString().equals("value") || p.getNameAsString().equals("path"))
                            .findFirst()
                            .map(p -> p.getValue().toString())
                            .orElse("");
                    }

                    if (!path.isEmpty()) {
                        // Clean the path 
                        System.out.println("   Path: " + path.replaceAll("^\"|\"$", ""));
                    }
                    break;
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
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicSecurityThreatDetector {

    // Define known security  annotations
    private static final List<String> SECURITY_ANNOTATIONS = Arrays.asList(
            "PreAuthorize",
            "Secured",
            "RolesAllowed"
    );

    public static void main(String[] args) throws Exception {
        // Parse source codw
        File file = new File("flat_file.java");
        CompilationUnit cu = StaticJavaParser.parse(file);

        System.out.println("--- Starting API Security Analysis ---");

        //  Loop through all method - file 
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            // analyze methods w API endpoints
            if (method.getAnnotations().stream().anyMatch(a -> a.getNameAsString().endsWith("Mapping"))) {
                System.out.println("\n▶ Analyzing Endpoint: " + method.getNameAsString());
                System.out.println("   Signature: " + method.getDeclarationAsString());

                
                checkForMissingAuthentication(method);
                checkForMissingPagination(method);
                checkIfDeprecated(method);
                checkForEntityReturn(method);
            }
        }
        System.out.println("\n--- Analysis Complete ---");
    }

    
     // API2/API5 - endpoint for missing common security annotations.
     
    private static void checkForMissingAuthentication(MethodDeclaration method) {
        boolean hasSecurityAnnotation = method.getAnnotations().stream()
                .anyMatch(a -> SECURITY_ANNOTATIONS.contains(a.getNameAsString()));

        if (!hasSecurityAnnotation) {
            System.out.println("   ⚠️ WARNING (API2/API5): Endpoint has no explicit security annotation. May be publicly exposed.");
        }
    }

    
     // API4 endpoint returns a List without limits 
     
    private static void checkForMissingPagination(MethodDeclaration method) {
        String returnType = method.getType().asString();
        
        if (returnType.contains("List<") || returnType.contains("Collection<") || returnType.contains("Set<")) {
            Set<String> paramNames = method.getParameters().stream()
                    .map(p -> p.getName().asString())
                    .collect(Collectors.toSet());

            if (!paramNames.contains("page") && !paramNames.contains("size")) {
                System.out.println("   ⚠️ WARNING (API4): Endpoint returns a collection but lacks pagination parameters (e.g., 'page', 'size').");
            }
        }
    }
    
    // dpoint is marked as deprecated.
     
    private static void checkIfDeprecated(MethodDeclaration method) {
        if (method.isAnnotationPresent("Deprecated")) {
            System.out.println("   ⚠️ WARNING (API9): Endpoint is deprecated. Ensure it's tracked and scheduled for removal. ");
        }
    }

    
     //[API3] Checks if an endpoint returns a raw entity instead of a DTO.
    
    private static void checkForEntityReturn(MethodDeclaration method) {
        String returnType = method.getTypeAsString();
    
        if (returnType.equals("void") || returnType.matches("String|Integer|Long|Boolean|Double|ResponseEntity<Void>")) {
            return;
        }

        if (!returnType.endsWith("DTO") && !returnType.contains("DTO>")) {
            System.out.println("   ⚠️ WARNING (API3): Endpoint may be returning a raw entity ('" + returnType + "') instead of a DTO. ");
        }
    }
}

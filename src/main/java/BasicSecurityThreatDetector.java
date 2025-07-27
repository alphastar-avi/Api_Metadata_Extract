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

    // Define known security annotations. Add any from your framework.
    private static final List<String> SECURITY_ANNOTATIONS = Arrays.asList(
            "PreAuthorize",
            "Secured",
            "RolesAllowed"
    );

    public static void main(String[] args) throws Exception {
        // Parse the specified Java source file
        File file = new File("flat_file");
        CompilationUnit cu = StaticJavaParser.parse(file);

        System.out.println("--- Starting API Security Analysis ---");

        // Loop through all methods in the file
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            // Only analyze methods that are API endpoints
            if (method.getAnnotations().stream().anyMatch(a -> a.getNameAsString().endsWith("Mapping"))) {
                System.out.println("\n▶ Analyzing Endpoint: " + method.getNameAsString());
                System.out.println("   Signature: " + method.getDeclarationAsString());

                // --- Run Security Checks ---
                checkForMissingAuthentication(method);
                checkForMissingPagination(method);
                checkIfDeprecated(method);
                checkForEntityReturn(method);
            }
        }
        System.out.println("\n--- Analysis Complete ---");
    }

    /**
     * [API2/API5] Checks if an endpoint is missing common security annotations.
     */
    private static void checkForMissingAuthentication(MethodDeclaration method) {
        boolean hasSecurityAnnotation = method.getAnnotations().stream()
                .anyMatch(a -> SECURITY_ANNOTATIONS.contains(a.getNameAsString()));

        if (!hasSecurityAnnotation) {
            System.out.println("   ⚠️ WARNING (API2/API5): Endpoint has no explicit security annotation. May be publicly exposed.");
        }
    }

    /**
     * [API4] Checks if an endpoint returns a List without pagination parameters.
     */
    private static void checkForMissingPagination(MethodDeclaration method) {
        String returnType = method.getType().asString();
        // Updated to also check inside generic wrappers like ResponseEntity
        if (returnType.contains("List<") || returnType.contains("Collection<") || returnType.contains("Set<")) {
            Set<String> paramNames = method.getParameters().stream()
                    .map(p -> p.getName().asString())
                    .collect(Collectors.toSet());

            if (!paramNames.contains("page") && !paramNames.contains("size")) {
                System.out.println("   ⚠️ WARNING (API4): Endpoint returns a collection but lacks pagination parameters (e.g., 'page', 'size').");
            }
        }
    }
    
    /**
     * [API9] Checks if an endpoint is marked as deprecated.
     */
    private static void checkIfDeprecated(MethodDeclaration method) {
        if (method.isAnnotationPresent("Deprecated")) {
            System.out.println("   ⚠️ WARNING (API9): Endpoint is deprecated. Ensure it's tracked and scheduled for removal.");
        }
    }

    /**
     * [API3] Checks if an endpoint returns a raw entity instead of a DTO.
     * This is a heuristic check assuming DTOs end with "DTO".
     */
    private static void checkForEntityReturn(MethodDeclaration method) {
        String returnType = method.getTypeAsString();
        // Ignore primitive types, void, and common Java types
        if (returnType.equals("void") || returnType.matches("String|Integer|Long|Boolean|Double|ResponseEntity<Void>")) {
            return;
        }

        if (!returnType.endsWith("DTO") && !returnType.contains("DTO>")) {
            System.out.println("   ⚠️ WARNING (API3): Endpoint may be returning a raw entity ('" + returnType + "') instead of a DTO.");
        }
    }
}
# API Metadata Extractor (JavaParser)

This project extracts and prints metadata for Spring Boot REST API methods from a Java source file using JavaParser.

<img width="3840" height="1800" alt="Sample AST" src="https://github.com/user-attachments/assets/514067fe-4a19-4fa0-b545-df0323c41b07" />


## Features
- Extracts HTTP method annotations (e.g., `@GetMapping`)
- Extracts API path (e.g., `/users/{id}`)
- Prints method name, return type, and parameters (with types and annotations)

## Usage

1. **Install dependencies:**
   ```sh
   mvn compile
   ```

2. **Run the extractor:**
   ```sh
   mvn exec:java -Dexec.mainClass=ApiMetadataExtractor
   ```

3. **Customize:**
   - By default, it parses `ApiController.java` in the project root. Change the file path in `ApiMetadataExtractor.java` as needed.

## Example Output

<img width="975" height="393" alt="Screenshot 2025-07-21 at 7 11 39 PM" src="https://github.com/user-attachments/assets/0b771bdc-44a8-4f10-ba7f-cdd88fcbc606" />

---
## BasicSecurityThreatDetector

provides partial checks for the following threats:

**API2: Broken Authentication & API5:** Broken Function Level Authorization: It checks if an endpoint is missing any security annotation, which is a strong indicator of these issues.

**API3: Broken Object Property Level Authorization:** It uses a simple heuristic (checking if the return type is a DTO) to guess if you might be exposing full data models.

**API4: Unrestricted Resource Consumption:** It checks for a very common pattern: returning a list of items without pagination parameters.

**API9: Improper Inventory Management:** The @Deprecated check helps identify outdated endpoints that should be removed from the inventory.

**Example Output**

<img width="1064" height="236" alt="Screenshot 2025-07-27 at 4 50 27 PM" src="https://github.com/user-attachments/assets/86590f01-6f1d-47a1-96b8-7f30d4d4139a" />


---
<img width="1564" height="3840" alt="API extract flow" src="https://github.com/user-attachments/assets/ef28edb9-50a6-41e8-8e5c-82144a26c9c1" />


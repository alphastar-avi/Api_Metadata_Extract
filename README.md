# API Metadata Extractor (JavaParser)

This project extracts and prints metadata for Spring Boot REST API methods from a Java source file using JavaParser.

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
```
▶ Method: getUser
   Return Type: ResponseEntity<User>
   Annotation: GetMapping
   Path: /users/{id}
   Param: id (Long)
      PathVariable
   Param: role (String)
      RequestParam

▶ Method: addProduct
   Return Type: ResponseEntity<Product>
   Annotation: PostMapping
   Path: /products
   Param: product (Product)
      RequestBody
```

---


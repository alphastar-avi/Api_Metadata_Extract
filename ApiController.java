package com.example.demo.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.
*;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/api")
public class ApiController {
// 1. GET /api/users/{id}
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
User user = userService.getUserById(id);
return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
}
// 2. GET /api/users?role=admin
@GetMapping("/users")
public ResponseEntity<List<User>> getUsersByRole(@RequestParam String role) {
List<User> users = userService.getUsersByRole(role);
return ResponseEntity.ok(users);
}
// 3. POST /api/users
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {
User created = userService.createUser(user);
return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
// 4. PUT /api/users/{id}
@PutMapping("/users/{id}")
public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User
user) {
User updated = userService.updateUser(id, user);
return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
}
// 5. GET /api/orders/{orderId}?details=true
@GetMapping("/orders/{orderId}")
public ResponseEntity<Order> getOrder(
@PathVariable Long orderId,
@RequestParam(defaultValue = "false") boolean details) {
Order order = orderService.getOrder(orderId, details);
return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
}
// 6. POST /api/products
@PostMapping("/products")
public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
Product saved = productService.addProduct(product);
return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
// 7. GET /api/products/{productId}
@GetMapping("/products/{productId}")
public ResponseEntity<Product> getProduct(@PathVariable String productId) {
Product product = productService.getProductById(productId);
return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
}
// 8. PUT /api/products/{productId}/price
@PutMapping("/products/{productId}/price")
public ResponseEntity<Product> updatePrice(
@PathVariable String productId,
@RequestParam double price) {
Product updated = productService.updatePrice(productId, price);
return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
}
// 9. POST /api/reports/search
@PostMapping("/reports/search")
public ResponseEntity<List<Report>> searchReports(@RequestBody ReportSearchFilter
filter) {
List<Report> results = reportService.searchReports(filter);
return ResponseEntity.ok(results);
}
// 10. PUT /api/settings
@PutMapping("/settings")
public ResponseEntity<Void> updateSettings(@RequestBody SystemSettings settings) {
settingsService.applySettings(settings);
return ResponseEntity.noContent().build();
}
// Mocked service layer references (for compilation)
private UserService userService = new UserService();
private OrderService orderService = new OrderService();
private ProductService productService = new ProductService();
private ReportService reportService = new ReportService();
private SettingsService settingsService = new SettingsService();
}
// Model class stubs
class User { Long id; String name; String email; }
class Order { Long id; List<String> items; }
class Product { String id; String name; double price; }
class ReportSearchFilter { String type; Date from; Date to; }
class SystemSettings { boolean maintenanceMode; String timezone; }
// Service class stubs
class UserService {
User getUserById(Long id) { return null; }
List<User> getUsersByRole(String role) { return null; }
User createUser(User user) { return null; }
User updateUser(Long id, User user) { return null; }
}
class OrderService {
Order getOrder(Long id, boolean details) { return null; }
}
class ProductService {
Product addProduct(Product p) { return null; }
Product getProductById(String id) { return null; }
Product updatePrice(String id, double price) { return null; }
}
class ReportService {
List<Report> searchReports(ReportSearchFilter f) { return null; }
}
class SettingsService {
void applySettings(SystemSettings s) {}
}
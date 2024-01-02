package antifraud.controller;

import antifraud.entity.User;
import antifraud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User created = userService.create(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username) {
        userService.delete(username);
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("status", "Deleted successfully!");
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = {"/access", "/access/"})
    public ResponseEntity<Map<String, String>> setUserAccess(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String operation = body.get("operation");
        userService.setLocked(username, operation);
        return ResponseEntity.ok(Map.of("status", String.format("User %s %sed!", username, operation.toLowerCase())));
    }

    @PutMapping("/role")
    public User setUserRole(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String role = body.get("role");
        return userService.setRole(username, role);
    }
}

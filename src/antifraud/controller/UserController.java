package antifraud.controller;

import antifraud.entity.User;
import antifraud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User created = userService.create(user);
        if (null != created) {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        throw new NullPointerException();
    }

    @GetMapping("/list")
    public List<User> getUsers() {
        return userService.getUsers();
    }
}

package mp.BookingSystem.controller;

import mp.BookingSystem.model.User;
import mp.BookingSystem.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return authService.createUser(user);
    }
}

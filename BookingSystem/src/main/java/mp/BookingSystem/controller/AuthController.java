package mp.BookingSystem.controller;

import mp.BookingSystem.dto.LoginRequest;
import mp.BookingSystem.dto.LoginResponse;
import mp.BookingSystem.model.User;
import mp.BookingSystem.repository.UserRepository;
import mp.BookingSystem.service.AuthService;
import mp.BookingSystem.service.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthController(AuthService authService,UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JWTService jwtService) {

        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.register(user);
    }

    //  LOGIN METHOD HERE
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository
                .findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.getJWTToken(user.getUserName());

        return new LoginResponse(token);
    }
}

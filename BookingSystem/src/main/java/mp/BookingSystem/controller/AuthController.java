package mp.BookingSystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import mp.BookingSystem.dto.LoginRequest;
import mp.BookingSystem.dto.LoginResponse;
import mp.BookingSystem.model.BlacklistedToken;
import mp.BookingSystem.model.RefreshToken;
import mp.BookingSystem.model.User;
import mp.BookingSystem.repository.BlacklistRepository;
import mp.BookingSystem.repository.RefreshTokenRepository;
import mp.BookingSystem.repository.UserRepository;
import mp.BookingSystem.service.AuthService;
import mp.BookingSystem.service.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final RefreshTokenRepository refreshRepo;
    private final BlacklistRepository blacklistRepo;

    public AuthController(AuthService authService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JWTService jwtService,
                          RefreshTokenRepository refreshRepo,
                          BlacklistRepository blacklistRepo) {

        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshRepo = refreshRepo;
        this.blacklistRepo = blacklistRepo;
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

        String accessToken = jwtService.generateAccessToken(user.getUserName());
        String refreshToken = jwtService.generateRefreshToken(user.getUserName());

        return new LoginResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestParam String refreshToken) {

        RefreshTokenRepository refreshRepo = null;
        RefreshToken token = refreshRepo.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Refresh token expired");
        }

        String newAccess = jwtService.generateAccessToken(token.getUsername());

        return new LoginResponse(newAccess, refreshToken);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("No token found");
        }

        String token = header.substring(7);

        BlacklistedToken blacklisted = new BlacklistedToken();
        blacklisted.setToken(token);

        blacklistRepo.save(blacklisted);

        return "Logged out successfully";
    }



}

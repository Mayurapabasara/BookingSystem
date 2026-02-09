package mp.BookingSystem.controller;

import mp.BookingSystem.model.User;
import mp.BookingSystem.repository.UserRepository;
import mp.BookingSystem.service.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HomeController(JWTService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getHellow(){
        return "Hello World";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam String username){
//        return jwtService.getJWTToken(username);
//    }


    @PostMapping("/login")
    public String login(@RequestBody User loginUser) {

        User user = userRepository
                .findByUserName(loginUser.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.getJWTToken(user.getUserName());
    }




    @GetMapping("/username")
    public String getUsername(@RequestParam String token){
        return jwtService.getUsername(token);
    } //token aken username genimata yoda gane

}

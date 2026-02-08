package mp.BookingSystem.controller;

import mp.BookingSystem.dto.LoginRequest;
import mp.BookingSystem.service.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public HomeController(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public String getHellow(){
        return "Hello World";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password){
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );
//        return jwtService.generateToken(username);
//    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return jwtService.generateToken(request.getUsername());
    }



    @GetMapping("/username")
    public String getUsername(@RequestParam String token){
        return jwtService.extractUsername(token);
    } //token aken username genimata yoda gane

}

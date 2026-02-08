package mp.BookingSystem.service;

import mp.BookingSystem.model.User;
import mp.BookingSystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User userData){
        User newUser = new User(
                userData.getFullName(),
                userData.getEmail(),
                userData.getUserName(),
                passwordEncoder.encode(userData.getPassword()));

        return userRepository.save(newUser);
    }
}

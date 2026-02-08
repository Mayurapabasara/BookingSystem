package mp.BookingSystem.service;

import mp.BookingSystem.model.User;
import mp.BookingSystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


//@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        mp.BookingSystem.model.User userData =
                userRepository.findByUserName(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(userData.getUserName()) //get the value from database
                .password(userData.getPassword()) //get the value from database
                .roles("USER")
                .build();
    }
}

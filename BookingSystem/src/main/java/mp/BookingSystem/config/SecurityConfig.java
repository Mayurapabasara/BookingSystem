package mp.BookingSystem.config;

import mp.BookingSystem.filter.JWDFilter;
import mp.BookingSystem.repository.UserRepository;
import mp.BookingSystem.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<<<<<<< Updated upstream
import org.springframework.security.authentication.AuthenticationManager;
=======
>>>>>>> Stashed changes
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JWDFilter jwdFilter;

    public SecurityConfig(UserRepository userRepository, JWDFilter jwdFilter) {
        this.userRepository = userRepository;
        this.jwdFilter = jwdFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwdFilter, UsernamePasswordAuthenticationFilter.class)
<<<<<<< Updated upstream
                //.authenticationProvider(authenticationProvider)
                //.httpBasic(Customizer.withDefaults())
=======
                .authenticationProvider(authenticationProvider)
                .httpBasic(Customizer.withDefaults())
>>>>>>> Stashed changes
                .build();
    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//        //.setUserDetailsService(userDetailsService());
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService(userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

package mp.BookingSystem.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mp.BookingSystem.model.User;
import mp.BookingSystem.repository.UserRepository;
import mp.BookingSystem.service.JWTService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWDFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    public JWDFilter(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull  FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        // 1️⃣ No header → continue filter chain
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ Extract token properly
        String jwt_tokenToken = authorization.substring(7);
        String username = jwtService.getUsername(jwt_tokenToken);


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByUserName(username).orElse(null);

            if (user != null) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                java.util.List.of(() -> "ROLE_" + user.getRole())
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}

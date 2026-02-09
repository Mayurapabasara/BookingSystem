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

        if (username == null) {
            filterChain.doFilter(request,response);
            return;
        }

        // 3️⃣ If already authenticated → skip
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        User userData = userRepository.findByUserName(username).orElse(null);

        if (userData == null) {
            filterChain.doFilter(request,response);
            return;
        }

        //if (SecurityContextHolder.getContext().getAuthentication() != null) filterChain.doFilter(request,response);

        //4️⃣ Create UserDetails (IMPORTANT: Use Spring Security User)
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(userData.getUserName())
                .password(userData.getPassword())
                .authorities("USER") // add roles if needed
                .build();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(token);
        //System.out.println(jwt_token);
        filterChain.doFilter(request, response);
    }
}

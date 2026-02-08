package mp.BookingSystem.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTService {

    private final SecretKey secretKey;

    /* Generate Key */
    public JWTService() {
//        try{
//            SecretKey k = KeyGenerator.getInstance("HmacSHA256").generateKey();
//            secretKey = Keys.hmacShaKeyFor(k.getEncoded());
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
        this.secretKey = Keys.hmacShaKeyFor(
                "my-super-secret-key-my-super-secret-key"
                        .getBytes()
        );
    }

    /*Generate token*/
    public String generateToken(String username){
        return Jwts.builder()
                //.subject("Booking System")
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(secretKey)
                .compact();
    }

    /* get the username from to JWT token */
    public String extractUsername(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e){
            return null; //return "Invalied token";
        }
    }

}







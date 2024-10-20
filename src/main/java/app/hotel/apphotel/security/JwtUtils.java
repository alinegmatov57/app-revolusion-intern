package app.hotel.apphotel.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.expire.time}")
    private Integer expireTime;

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + expireTime))
                .claim("username", username)
                .signWith(secretKey)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build();
            parser.parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build();
            Jwt jwt = parser.parse(token);
            if (jwt.getBody() instanceof Claims) {
                return ((Claims) jwt.getBody()).getSubject();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

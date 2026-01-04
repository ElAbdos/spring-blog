package abdel.springblog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenValidator {

    private static final String SECRET_KEY = "VyAUR86udZI2xhN3ubzuSiB7J9nAP86nglxcA5QmflO";
    private final SecretKey key;

    public TokenValidator() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Valider le token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extraire le nom d'utilisateur depuis le token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    // Extraire les rôles depuis le token
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        String roles = claims.get("roles", String.class);
        return Arrays.stream(roles.split(",")).collect(Collectors.toList());
    }
}


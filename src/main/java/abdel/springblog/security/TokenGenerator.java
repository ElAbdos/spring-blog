package abdel.springblog.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenGenerator {

    private static final String SECRET_KEY = "VyAUR86udZI2xhN3ubzuSiB7J9nAP86nglxcA5QmflO";
    private static final long EXPIRATION_TIME = 86400000;

    private final SecretKey key;

    public TokenGenerator() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder().subject(username).claim("roles", roles).issuedAt(now).expiration(expiryDate).signWith(key).compact();
    }
}


package abdel.springblog.controllers;

import abdel.springblog.security.JwtDTO;
import abdel.springblog.security.TokenGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;

    public AuthController(AuthenticationManager authenticationManager, TokenGenerator tokenGenerator) {
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenGenerator.generateToken(authentication);
            return ResponseEntity.ok(new JwtDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Identifiants invalides"));
        }
    }
}


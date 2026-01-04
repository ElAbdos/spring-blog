package abdel.springblog.controllers;

import abdel.springblog.dto.UserRegisterDto;
import abdel.springblog.models.Role;
import abdel.springblog.models.User;
import abdel.springblog.repositories.UserRepository;
import abdel.springblog.security.JwtDTO;
import abdel.springblog.security.TokenGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, TokenGenerator tokenGenerator, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.status(400).body(Map.of("error", "Ce nom d'utilisateur est déjà pris"));
        }
        Role role;
        try {
            role = Role.valueOf(dto.getRole().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Rôle invalide. Utilisez: READER, PUBLISHER ou MODERATOR"));
        }
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setRole(role);
        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "Inscription réussie", "username", newUser.getUsername(), "role", newUser.getRole().toString()));
    }
}


package abdel.springblog.security;

import abdel.springblog.models.User;
import abdel.springblog.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class R505UserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public R505UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername()).password(user.getPassword()).authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))).build();
    }
}


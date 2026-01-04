package abdel.springblog.security;

import lombok.Getter;

@Getter
public class JwtDTO {
    private final String token;
    private final String type = "Bearer";

    public JwtDTO(String token) {
        this.token = token;
    }
}


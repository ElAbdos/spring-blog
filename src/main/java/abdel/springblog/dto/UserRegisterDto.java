package abdel.springblog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {
    private String username;
    private String password;
    private String role;

    public UserRegisterDto() {}
}


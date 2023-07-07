package com.digiron.hype.v1.requests;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    @NotBlank
    @Size(min = 1, max = 250)
    private String password;
    private String firstname;
    private String lastname;
    private String email;

    public LoginRequest toLoginRequest() {
        return new LoginRequest(username, password);
    }
}

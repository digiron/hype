package com.digiron.hype.v1.requests;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AdminSignupRequest extends SignupRequest{
    @NotBlank
    private String applicationAdminPassword;
}

package com.puscas.authentication.controller.model;

import com.puscas.authentication.controller.validation.PasswordMatches;
import com.puscas.authentication.controller.validation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@PasswordMatches
public class UserDto {
    @NonNull
    @NotEmpty
    private String firstName;

    @NonNull
    @NotEmpty
    private String lastName;

    @NonNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @ValidEmail
    @NonNull
    @NotEmpty
    private String email;
    
    // standard getters and setters
}

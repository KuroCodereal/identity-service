package com.example.identity_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AuthenticationRequest {

    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 3, max = 30, message = "USERNAME_INVALID")
    String Username;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 64, message = "PASSWORD_INVALID")
    String Password;
}

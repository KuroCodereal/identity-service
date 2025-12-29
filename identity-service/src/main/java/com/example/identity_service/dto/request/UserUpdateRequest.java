package com.example.identity_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import validator.DobConstraint;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 8, max = 64, message = "PASSWORD_INVALID")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "PASSWORD_WEAK"
    )
    String password;

    @Size(max = 50, message = "FIRSTNAME_TOO_LONG")
    String firstName;

    @Size(max = 50, message = "LASTNAME_TOO_LONG")
    String lastName;

    @DobConstraint(min = 18,message = "INVALID_DOB")
    LocalDate dob;

    @NotEmpty(message = "ROLES_EMPTY")
    List<String> roles;

}

package com.example.identity_service.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//verify token xem co phai token hop le cua he thong k
public class IntrospectRequest {

    @NotBlank(message = "TOKEN_REQUIRED")
    @Size(min = 20, max = 2048, message = "TOKEN_INVALID_LENGTH")
    @Pattern(
            regexp = "^[A-Za-z0-9\\-_.]+$",
            message = "TOKEN_INVALID_FORMAT"
    )
    String token;
}

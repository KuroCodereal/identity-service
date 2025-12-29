package com.example.identity_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class PermissionRequest {
    @NotBlank(message = "PERMISSION_NAME_REQUIRED")
    @Size(min = 3, max = 50, message = "PERMISSION_NAME_INVALID")
    @Pattern(
            regexp = "^[A-Z_]+$",
            message = "PERMISSION_NAME_FORMAT_INVALID"
    )
    String name;

    @Size(max = 255, message = "PERMISSION_DESCRIPTION_TOO_LONG")
    String description;

}

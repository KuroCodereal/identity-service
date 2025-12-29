package com.example.identity_service.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    @NotBlank(message = "ROLE_NAME_REQUIRED")
    @Size(min = 3, max = 30, message = "ROLE_NAME_INVALID")
    @Pattern(
            regexp = "^[A-Z_]+$",
            message = "ROLE_NAME_FORMAT_INVALID"
    )
    String name;

    @Size(max = 255, message = "ROLE_DESCRIPTION_TOO_LONG")
    String description;

    @NotEmpty(message = "PERMISSIONS_EMPTY")
    // role request nhan set<String> cua permissions nhung entity la Set<Permission>
    Set<String> permissions;

}

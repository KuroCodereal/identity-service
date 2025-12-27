package com.example.identity_service.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String description;
    // role request nhan set<String> cua permissions nhung entity la Set<Permission>
    Set<String> permissions;

}

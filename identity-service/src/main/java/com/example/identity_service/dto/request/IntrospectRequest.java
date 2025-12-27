package com.example.identity_service.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//verify token xem co phai token hop le cua he thong k
public class IntrospectRequest {
    String token;
}

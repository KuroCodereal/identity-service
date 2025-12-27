package com.example.identity_service.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//nhung field nao null se ko kem vao trong json tra ve
@JsonInclude(JsonInclude.Include.NON_NULL)
// chi anh huong den cac truong field
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T>{
    @Builder.Default
     int code = 1000;
     String message;
     T result;


}

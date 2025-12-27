package com.example.identity_service.dto.request;

import jakarta.validation.constraints.Size;

// tu dong generate getter/setter cho cac private field
import lombok.*;
import lombok.experimental.FieldDefaults;
import validator.DobConstraint;

import java.time.LocalDate;

// tu dong generate getter/setter, ham constructor co tat ca cac attribute va ham toString vv
@Data
//co 2 constructor
@NoArgsConstructor
@AllArgsConstructor

// co the tao object luc co 1 trong cac data thuoc tinh nhat dinh chu ko can co het tuc la constructor ko co tat ca cac thuoc tinh cua object
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;


    @DobConstraint(min = 18,message = "INVALID_DOB")
    LocalDate dob;


}

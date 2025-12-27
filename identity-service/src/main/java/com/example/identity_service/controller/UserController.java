package com.example.identity_service.controller;

import com.example.identity_service.dto.request.ApiResponse;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
//makeFinal = true la de tao dc constructor cho cac bien final
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Log4j2

//phan biet @RequiredArgsConstructor va @Autowired
public class UserController {
    //controller chi goi service con service chi dc goi repository
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(userService.createUser(request));
//        return apiResponse;
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {

        // get thong tin hien tai dang dc authenticate trong 1 request
        // chua thong tin ve user dang dang nhap hien tai
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username:{}",authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority:{}",grantedAuthority.getAuthority()));

//        return userService.getUsers();
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        //return userService.getUserById(userId);
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/{myInfo}")
    ApiResponse<UserResponse> getMyInfo() {
        //return userService.getUserById(userId);
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        //return userService.updateUser(userId, request);
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
//        userService.deleteUser(userId);
//        return "User has been deleted";
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }
}



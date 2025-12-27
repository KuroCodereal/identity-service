package com.example.identity_service.mapper;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

//Mapper theo kieu dependency injection
@Mapper(componentModel = "spring")
public interface UserMapper {
    // map tu UserCreationRequest sang User
    User toUser(UserCreationRequest request);
    //chuyen firstName thanh giong lastName
    //@Mapping(source = "firstName",target = "lastName")
    //ignore lastName thanh null
    @Mapping(target = "lastName",ignore = true)
    UserResponse toUserResponse(User user);
    // Map data tu UserUpdateRequest sang User user
    @Mapping(target = "roles", ignore = true)
    User updateUser(@MappingTarget User user, UserUpdateRequest request);
}

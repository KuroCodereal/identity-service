package com.example.identity_service.enums;

public enum Role {
    ADMIN,
    USER
}

//Permission (Privilege)
//    - createPost
//    - UpdatePost

// 1 user co nhieu role, trong role co nhieu permission
// User -> many Role
// Role -> many Permission
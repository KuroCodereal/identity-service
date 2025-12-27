package com.example.identity_service.service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.enums.Role;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service

//tao ra 1 constructor chua tat ca cac bien final (bien final tu dong fill thanh final luon)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    //đánh dấu cho Spring biết rằng sẽ tự động inject bean tương ứng vào vị trí được đánh dấu mà ko cần phải tự tạo các method dưới đây
    // public UserRepository(UserRepository userRepository) {
    //     this.userRepository = userRepository;
    // }

    // GET
    // SET
    // ko phai best practice de inject bean vao object, dung lombock tien hon
    //@Autowired
    UserRepository userRepository;
    RoleRepository roleRepository;

    //@Autowired
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request){


        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // co the tao object luc co 1 trong cac data thuoc tinh nhat dinh chu ko can co het
        UserCreationRequest request1 = UserCreationRequest.builder()
                .username("")
                .firstName("")
                .build();

        //Map data giua cac dto voi nhau dung mapstruct thi code se ngan di

//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        User user = userMapper.toUser(request);
        // do manh cua giai ma, neu qua lon thi thuat toan se chay rat lau
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        //user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    // tao proxy ngay trc ham nay, truoc luc goi ham se check
    public List<UserResponse> getUsers(){
        log.info("In Method get Users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    // check sau khi method duoc thuc hien xong, neu tman thi return ko thi chan lai
    // chi lay duoc thong tin cua user nay chu ko dc lay cua user khac
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id){
        log.info("In Method getUserById");
        return userMapper.toUserResponse(userRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse getMyInfo(){
        // biết được người dùng hiện tại là ai sau khi họ đã đăng nhập thành công
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request){
          User user = userRepository.findById(userId)
                  .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        userMapper.updateUser(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}

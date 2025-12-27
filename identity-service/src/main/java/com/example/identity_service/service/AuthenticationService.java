package com.example.identity_service.service;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.IntrospectRequest;
import com.example.identity_service.dto.response.AuthenticationResponse;
import com.example.identity_service.dto.response.IntrospectResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    // de no ko inject vao constructor
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        try {
            // 1. Phân tích cú pháp (parse) token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Khởi tạo JWSVerifier (sử dụng MACVerifier như trong code gốc)
            // **Lưu ý:** SIGNER_KEY phải là một khóa bí mật đủ mạnh
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            // 2. Xác thực chữ ký (verification)
            // Lỗi gốc: verifier.verify(verifier)
            // Sửa: Phải gọi phương thức verify trên đối tượng signedJWT và truyền verifier vào
            boolean verified = signedJWT.verify(verifier);

            // 3. Kiểm tra thời gian hết hạn (expiration time)
            Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            // Kiểm tra token có được xác thực và chưa hết hạn không
            boolean notExpired = expiredTime == null || expiredTime.after(new Date());

            return IntrospectResponse.builder()
                    .valid(verified && notExpired)
                    .build();
        } catch (ParseException | JOSEException e) {
            // Xử lý các lỗi có thể xảy ra trong quá trình parse hoặc verify
            // Trả về false nếu có lỗi xảy ra
            return IntrospectResponse.builder()
                    .valid(false)
                    .build();
        }
    }


    private String generateToken(User user){

        //b1: tao heacer
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // cac data trong body goi la claims
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                // dai dien cho user dang nhap
                .subject(user.getUsername())
                // token nay issue tu ai (nguoi phat hanh thuong la ten doamin)
                .issuer("")
                .issueTime(new Date())
                //ton tai trong thoi han bao lau
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                //them data (claims khac)
                .claim("scope",buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        // symmetric signature
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }
        catch (JOSEException e) {
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }

    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });

        return stringJoiner.toString();
    }
}

    package com.example.identity_service.configuration;

    import com.example.identity_service.enums.Role;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
    import org.springframework.security.oauth2.jwt.JwtDecoder;
    import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
    import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
    import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
    import org.springframework.security.web.SecurityFilterChain;

    import javax.crypto.spec.SecretKeySpec;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    public class SecurityConfig {

        private final String[] PUBLIC_ENDPOINTS = {
                "/users","/auth/token","/auth/introspect"
        };

        @Value("${jwt.signerKey}")
        private String signerKey;

        // AUTHENTICATION VOI JWT BANG 1 CAI FILTER ko tuan theo architecture cua spring, neu dung spring security r thi architecture rat quan trong
        // loi 401 chi duoc xu ly o day vi no nam trong tang filter ko xu ly trong globalexceptionhandler dc

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.authorizeHttpRequests(request ->
                    request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS).permitAll()
                            // co scope admin thi moi truy cap dc
                            .requestMatchers(HttpMethod.GET,"/users")
                            // customize roi nhung luc dau mac dinh la SCOPE_ADMIN
                            //.hasAuthority("ROLE_ADMIN")
                            .hasRole(Role.ADMIN.name())
                            .anyRequest().authenticated());

            httpSecurity.oauth2ResourceServer(
                    // muon dky 1 authenticationProvider
                    oauth2 -> oauth2.jwt(jwtConfigurer ->
                            jwtConfigurer.decoder(jwtDecoder())
                                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                            // new cung dc ko can bean cua JwtAuthenticationEntryPoint
                            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
            );

            //bao ve api khoi cross site -> tat
            httpSecurity.csrf(AbstractHttpConfigurer::disable);

            return httpSecurity.build();
        }

        JwtAuthenticationConverter jwtAuthenticationConverter(){
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            // co the customize authorities tuy thich
            jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

            JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
            converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
            return converter;
        }
        @Bean
        JwtDecoder jwtDecoder() {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");

            //Nimbus chinh la thu vien dung de ma hoa token
            return NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }


    }

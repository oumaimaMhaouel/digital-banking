package com.example.digitalbanking.web;

import com.example.digitalbanking.entities.AppUser;
import com.example.digitalbanking.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        //UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
       // String token = jwtService.generateToken(userDetails);
        String scope= authentication.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));
        Instant instant = Instant.now();
        JwtClaimsSet   jwtClaimsSet = JwtClaimsSet
                .builder().issuedAt(instant)
                .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
                .subject(loginRequest.getUsername())
                .claim("scope",scope)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(),
                jwtClaimsSet);
        String  jwt=jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        return Map.of("access-token", jwt);
    }


    @GetMapping("/profile")
    public Authentication login(Authentication authentication) {
        return authentication;
    }

/*
    @PostMapping("/register")
    public AppUser register(@RequestBody RegisterRequest registerRequest) {
        AppUser appUser = AppUser.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(List.of("USER"))
                .build();
        return appUserRepository.save(appUser);
    }*/

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
    }
}

package com.rossypotentials.springsecurityjwt.service.impl;

import com.rossypotentials.springsecurityjwt.dto.RequestResponse;
import com.rossypotentials.springsecurityjwt.entity.UserModel;
import com.rossypotentials.springsecurityjwt.exception.UsernameNotFoundException;
import com.rossypotentials.springsecurityjwt.repository.UserRepository;
import com.rossypotentials.springsecurityjwt.service.AuthService;
import com.rossypotentials.springsecurityjwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public RequestResponse signUp(RequestResponse registrationRequest) {
        RequestResponse response = RequestResponse.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .build();
        UserModel userModel = UserModel.builder()
                .fullName(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(registrationRequest.getRole())
                .build();

        UserModel savedUserModel = userRepository.save(userModel);

        if (savedUserModel != null && savedUserModel.getId() > 0) {
            //response.setOurUsers(savedUserModel);
            response.setMessage("User Saved Successfully");
            response.setStatusCode(200);
        } else {
            response.setStatusCode(500);

        }
        return response;
    }

    public RequestResponse signIn(RequestResponse signInRequest) {
        RequestResponse response;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow();
            System.out.println("USER IS: " + user);
            var jwt = jwtUtils.generateToken(user);

            response = RequestResponse.builder()
                    .statusCode(200)
                    .token(jwt)
                    .expirationDate("24Hrs")
                    .message("Successfully Signed In")
                    .build();
        } catch (Exception e) {
            response = RequestResponse.builder()
                    .statusCode(500)
                    .error(e.getMessage())
                    .build();
        }

        return response;
    }



    public RequestResponse getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserModel user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return RequestResponse.builder()
                .name(user.getFullName())
                .email(user.getEmail())
                .message("User Details returned successfully")
                .statusCode(200)
                .build();
    }



}

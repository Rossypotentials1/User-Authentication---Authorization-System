package com.rossypotentials.springsecurityjwt;

import com.rossypotentials.springsecurityjwt.dto.RequestResponse;
import com.rossypotentials.springsecurityjwt.entity.UserModel;
import com.rossypotentials.springsecurityjwt.exception.UsernameNotFoundException;
import com.rossypotentials.springsecurityjwt.repository.UserRepository;
import com.rossypotentials.springsecurityjwt.service.impl.AuthServiceImpl;
import com.rossypotentials.springsecurityjwt.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp_Success() {
        RequestResponse registrationRequest = RequestResponse.builder()
                .name("Rose Nnena")
                .email("rose.nnena@example.com")
                .password("password")
                .role("USER")
                .build();

        UserModel userModel = UserModel.builder()
                .id(1)
                .fullName("Rose Nnena")
                .email("rose.nnena@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);


        RequestResponse response = authServiceImpl.signUp(registrationRequest);


        assertEquals(200, response.getStatusCode());
        assertEquals("User Saved Successfully", response.getMessage());
        verify(userRepository, times(1)).save(any(UserModel.class));
    }



    @Test
    void testSignUp_Failure() {
        RequestResponse registrationRequest = RequestResponse.builder()
                .name("Rose Nnena")
                .email("rose.nnena@example.com")
                .password("password")
                .role("USER")
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserModel.class))).thenReturn(null);

        RequestResponse response = authServiceImpl.signUp(registrationRequest);

        assertEquals(500, response.getStatusCode());
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    void testSignIn_Success() {
        RequestResponse signInRequest = RequestResponse.builder()
                .email("rose.nnena@example.com")
                .password("password")
                .build();

        UserModel userModel = UserModel.builder()
                .fullName("Rose Nnena")
                .email("rose.nnena@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userModel));
        when(jwtUtils.generateToken(any(UserModel.class))).thenReturn("jwtToken");

        // When
        RequestResponse response = authServiceImpl.signIn(signInRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("Successfully Signed In", response.getMessage());
        assertEquals("jwtToken", response.getToken());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void testSignIn_InvalidCredentials() {
        RequestResponse signInRequest = RequestResponse.builder()
                .email("rose.nnena@example.com")
                .password("wrongPassword")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        RequestResponse response = authServiceImpl.signIn(signInRequest);


        assertEquals(500, response.getStatusCode());
        assertEquals("Invalid credentials", response.getError());
        verify(userRepository, times(0)).findByEmail(anyString());
    }

    @Test
    void testGetAuthenticatedUserDetails_Success() {
        UserModel userModel = UserModel.builder()
                .fullName("Rose Nnena")
                .email("rose.nnena@example.com")
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName()).thenReturn("rose.nnena@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userModel));

        RequestResponse response = authServiceImpl.getAuthenticatedUserDetails();

        assertEquals(200, response.getStatusCode());
        assertEquals("Rose Nnena", response.getName());
        assertEquals("rose.nnena@example.com", response.getEmail());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void testGetAuthenticatedUserDetails_UserNotFound() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName()).thenReturn("rose.nnena@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            authServiceImpl.getAuthenticatedUserDetails();
        });

        verify(userRepository, times(1)).findByEmail(anyString());
    }
}

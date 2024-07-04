package com.rossypotentials.springsecurityjwt.service;

import com.rossypotentials.springsecurityjwt.dto.RequestResponse;

public interface AuthService {
    RequestResponse signUp(RequestResponse registrationRequest);
    RequestResponse signIn(RequestResponse signInRequest);
    RequestResponse getAuthenticatedUserDetails();

}

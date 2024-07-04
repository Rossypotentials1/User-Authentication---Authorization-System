package com.rossypotentials.springsecurityjwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserModelDetailsService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
}

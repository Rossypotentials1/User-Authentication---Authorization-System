package com.rossypotentials.springsecurityjwt.service.impl;

import com.rossypotentials.springsecurityjwt.repository.UserRepository;
import com.rossypotentials.springsecurityjwt.service.UserModelDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserModelDetailsServiceImpl implements UserModelDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow();
    }
}

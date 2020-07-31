package com.severo.demospring.service;


import com.severo.demospring.repository.DemoSpringUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DemosSpringUserDetailsService implements UserDetailsService {

    private final DemoSpringUserRepository demoSpringUserRepository;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return Optional.ofNullable(demoSpringUserRepository.findByUserName(userName))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

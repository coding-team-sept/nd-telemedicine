package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.repositories.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    AuthenticationRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final var appUser = repository
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
        return new User(
                appUser.getEmail(),
                appUser.getPassword(),
                List.of(new SimpleGrantedAuthority(appUser.getRole().getRole().name()))
        );
    }

    @Transactional
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return loadUserByUsername(email);
    }
}

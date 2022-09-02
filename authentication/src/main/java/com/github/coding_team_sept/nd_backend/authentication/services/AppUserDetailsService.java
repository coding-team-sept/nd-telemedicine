package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    AppUserRepository repository;

    @Override
    @Transactional
    public AppUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final var appUser = repository
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
        return AppUserDetails.fromAppUser(appUser);
    }

    @Transactional
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return loadUserByUsername(email);
    }
}

package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AppUserDetailsService is a class used to provide user details. It mainly converts
 * the AppUser from AppUserRepository to AppUserDetails.
 *
 * @author nivratig
 */
@Service
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    AppUserRepository repository;


    /**
     * This method retrieves the AppUserDetails from a given email. The "Username"
     * in this scenario is actually an email. In other words, email functions as the
     * username.
     *
     * @param email the email of a user
     * @return the user details
     * @throws UsernameNotFoundException is supposed to be EmailNotFoundException
     * @since 0.0.0-alpha.0
     */
    @Override
    @Transactional
    public AppUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final var appUser = repository
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
        return AppUserDetails.fromAppUser(appUser);
    }

    /**
     * This method wraps the loadUserByUsername method in order to prevent
     * confusion.
     *
     * @since 0.0.0-alpha.0
     */
    @Transactional
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return loadUserByUsername(email);
    }
}

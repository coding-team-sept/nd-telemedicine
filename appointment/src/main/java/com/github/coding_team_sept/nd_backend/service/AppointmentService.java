package com.github.coding_team_sept.nd_backend.service;

public class AppointmentService {
    public getDoctor(String dateTime) throws UsernameNotFoundException {
        final var appUser = repository
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));
        return new User(
                appUser.getEmail(),
                appUser.getPassword(),
                List.of(new SimpleGrantedAuthority(appUser.getRole().getName().name()))
        );
    }
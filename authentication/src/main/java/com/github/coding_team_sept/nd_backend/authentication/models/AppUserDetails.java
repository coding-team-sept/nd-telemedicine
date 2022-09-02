package com.github.coding_team_sept.nd_backend.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String name;
    @JsonIgnore
    private String password;
    private Role role;

    public static AppUserDetails fromAppUser(AppUser appUser) {
        return AppUserDetails.builder()
                .id(appUser.getId())
                .email(appUser.getEmail())
                .name(appUser.getName())
                .password(appUser.getPassword())
                .role(appUser.getRole())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName().name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final var appUserDetails = (AppUserDetails) o;
        return Objects.equals(id, appUserDetails.id);
    }
}

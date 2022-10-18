package com.github.coding_team_sept.nd_backend.authentication.components;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * DataLoader class populates the table inside the database when the service is started.
 *
 * @author nivratig
 * */
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private AppUserRepository authenticationRepo;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Arrays.stream(RoleType.values()).forEach(this::createRole); // Role must be created before admin/user
        createAdmin();
        alreadySetup = true;
    }

    @Transactional
    public void createRole(RoleType roleType) {
        final var role = roleRepo.findRoleByName(roleType);
        if (role.isEmpty()) {
            final var newRole = Role.builder().name(roleType).build();
            roleRepo.save(newRole);
        }
    }

    @Transactional
    public void createAdmin() {
        final var email = "admin@admin.com";
        final var appUser = authenticationRepo.findUserByEmail(email);
        if (appUser.isEmpty()) {
            final var newAppUser = AppUser
                    .builder()
                    .name("Admin")
                    .email(email)
                    .password(encoder.encode("admin123"))
                    .role(roleRepo.findRoleByName(RoleType.ROLE_ADMIN).orElse(null))
                    .build();
            authenticationRepo.save(newAppUser);
        }
    }
}

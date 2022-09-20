package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.RoleNotFoundException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.UserNotFoundException;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public record AppUserService(
        AppUserRepository appUserRepo,
        RoleRepository roleRepo
) {
    public List<UserDataResponse> getUserByRole(RoleType role) throws AppException {
        final var appUsers = appUserRepo.findAppUserByRole(
                roleRepo.findRoleByName(role)
                        .orElseThrow(RoleNotFoundException::new)
        );
        return appUsers.map(users -> users
                .stream()
                .map(appUser -> new UserDataResponse(appUser.getId(), appUser.getEmail(), appUser.getName()))
                .toList()
        ).orElseGet(List::of);
    }

    public UserDataResponse getUserById(Long id, RoleType role) throws AppException {
        final Optional<AppUser> appUser;
        if (role == null) {
            appUser = appUserRepo.findById(id);
        } else {
            appUser = appUserRepo.findAppUserByIdAndRole(
                    id,
                    roleRepo.findRoleByName(role)
                            .orElseThrow(RoleNotFoundException::new)
            );
        }
        return appUser.map(user -> new UserDataResponse(
                user.getId(),
                user.getEmail(),
                user.getName()
        )).orElseThrow(UserNotFoundException::new);
    }

    public List<UserDataResponse> getUsersByIds(List<Long> ids, RoleType role) throws AppException {
        final List<AppUser> appUsers;
        if (role == null) {
            appUsers = ids.stream()
                    .map(id -> appUserRepo.findById(id).orElse(null))
                    .filter(Objects::isNull)
                    .toList();
        } else {
            appUsers = ids.stream()
                    .map(id -> appUserRepo.findAppUserByIdAndRole(
                                    id,
                                    roleRepo.findRoleByName(role)
                                            .orElseThrow(RoleNotFoundException::new)
                            ).orElse(null)
                    ).filter(Objects::nonNull)
                    .toList();
        }
        return appUsers.stream()
                .map(appUser -> new UserDataResponse(
                        appUser.getId(),
                        appUser.getEmail(),
                        appUser.getName()
                )).toList();
    }
}

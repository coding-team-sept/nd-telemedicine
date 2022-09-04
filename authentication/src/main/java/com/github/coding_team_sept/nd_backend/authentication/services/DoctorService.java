package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.DoctorResponse;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record DoctorService(
        AppUserRepository appUserRepo,
        RoleRepository roleRepo
) {
    public List<DoctorResponse> getDoctor() {
        return appUserRepo.findAppUserByRole(
                        roleRepo.findRoleByName(RoleType.ROLE_DOCTOR).orElseThrow()
                ).orElseThrow()
                .stream()
                .map(appUser -> new DoctorResponse(appUser.getId(), appUser.getEmail(), appUser.getName()))
                .toList();
    }
}

package com.github.coding_team_sept.nd_backend.authentication.repositories;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findUserByEmail(String email);
    Boolean existsAppUserByEmail(String email);
    Optional<AppUser> findAppUserByIdAndRole(Long id, Role role);
    Optional<List<AppUser>> findAppUserByRole(Role role);
}

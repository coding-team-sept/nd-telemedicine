package com.github.coding_team_sept.nd_backend.authentication.repositories;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findUserByEmail(String email);
}

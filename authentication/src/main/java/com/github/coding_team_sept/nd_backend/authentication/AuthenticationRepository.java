package com.github.coding_team_sept.nd_backend.authentication;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<AppUser, Long> {
}

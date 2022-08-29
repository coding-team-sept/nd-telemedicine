package com.github.coding_team_sept.nd_backend.authentication.repositories;

import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}

package com.github.coding_team_sept.nd_backend.appointment.repositories;

import com.github.coding_team_sept.nd_backend.appointment.enums.SessionType;
import com.github.coding_team_sept.nd_backend.appointment.models.AppointmentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<AppointmentSession, Long> {
    Optional<AppointmentSession> findRoleByName(SessionType name);
}

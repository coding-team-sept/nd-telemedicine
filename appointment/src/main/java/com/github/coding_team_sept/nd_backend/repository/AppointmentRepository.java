package com.github.coding_team_sept.nd_backend.repository;

import com.github.coding_team_sept.nd_backend.model.DoctorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<DoctorUser, Long> {
    Optional<DoctorUser> getDoctor(Date dateTime);
}
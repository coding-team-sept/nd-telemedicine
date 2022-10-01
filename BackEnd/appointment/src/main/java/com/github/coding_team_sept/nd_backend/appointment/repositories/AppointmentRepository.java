package com.github.coding_team_sept.nd_backend.appointment.repositories;

import com.github.coding_team_sept.nd_backend.appointment.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> getAppointmentByAppointmentTimeBetween(Date start, Date end);

    boolean existsAppointmentByDoctorIdAndAppointmentTimeBetween(Long doctorId, Date start, Date end);

    boolean existsAppointmentByPatientIdAndAppointmentTimeBetween(Long patientId, Date start, Date end);

    List<Appointment> getAppointmentByPatientId(Long patientId);

    List<Appointment> getAppointmentByDoctorId(Long doctorId);
}
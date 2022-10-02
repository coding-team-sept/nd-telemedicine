package com.github.coding_team_sept.nd_backend.appointment.components;

import com.github.coding_team_sept.nd_backend.appointment.enums.SessionType;
import com.github.coding_team_sept.nd_backend.appointment.models.AppointmentSession;
import com.github.coding_team_sept.nd_backend.appointment.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * DataLoader class populates the table inside the database when the service is started.
 *
 * @author nivratig
 */
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    @Autowired
    private SessionRepository sessionRepo;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Arrays.stream(SessionType.values()).forEach(this::createSession); // Role must be created before admin/user
        alreadySetup = true;
    }

    @Transactional
    void createSession(SessionType sessionType) {
        final var role = sessionRepo.findRoleByName(sessionType);
        if (role.isEmpty()) {
            final var newRole = AppointmentSession.builder().name(sessionType).build();
            sessionRepo.save(newRole);
        }
    }
}

package com.github.coding_team_sept.nd_backend.chat.repositories;

import com.github.coding_team_sept.nd_backend.chat.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepositories extends JpaRepository<Message, Long> {
}

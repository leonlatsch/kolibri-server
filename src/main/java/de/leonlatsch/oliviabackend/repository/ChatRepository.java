package de.leonlatsch.oliviabackend.repository;

import de.leonlatsch.oliviabackend.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
}

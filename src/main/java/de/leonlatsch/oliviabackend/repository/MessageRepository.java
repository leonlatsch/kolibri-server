package de.leonlatsch.oliviabackend.repository;

import de.leonlatsch.oliviabackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
}

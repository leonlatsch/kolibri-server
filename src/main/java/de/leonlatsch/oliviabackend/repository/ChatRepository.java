package de.leonlatsch.oliviabackend.repository;

import de.leonlatsch.oliviabackend.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {

    Optional<Chat> findByFirstMemberAndSecondMember(String firstMember, String secondMember);

    Optional<Chat> findCidByCid(String cid);
}

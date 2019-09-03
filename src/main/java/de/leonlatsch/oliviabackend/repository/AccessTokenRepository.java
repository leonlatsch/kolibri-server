package de.leonlatsch.oliviabackend.repository;

import de.leonlatsch.oliviabackend.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {

    Optional<AccessToken> findByUid(int uid);
}

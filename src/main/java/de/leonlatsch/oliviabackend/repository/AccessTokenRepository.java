package de.leonlatsch.oliviabackend.repository;

import de.leonlatsch.oliviabackend.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {
}

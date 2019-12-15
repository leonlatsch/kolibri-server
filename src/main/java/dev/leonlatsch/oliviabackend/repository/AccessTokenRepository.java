package dev.leonlatsch.oliviabackend.repository;

import dev.leonlatsch.oliviabackend.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository to access the access_token table
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {

    Optional<AccessToken> findByUidAndValidTrue(String uid);
}

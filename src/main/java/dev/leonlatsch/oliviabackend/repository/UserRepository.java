package dev.leonlatsch.oliviabackend.repository;

import dev.leonlatsch.oliviabackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findTop100ByUidNotAndUsernameContaining(String uid, String username);

    Optional<User> findPublicKeyByUid(String uid);

    Optional<User> findUidByUid(String uid);
}

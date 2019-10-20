package de.leonlatsch.oliviabackend.repository;

import de.leonlatsch.oliviabackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameContaining(String username);

    List<User> findTop100ByUsernameContaining(String username);

    Optional<User> findPublicKeyByUid(int uid);

    Optional<User> findUidByUid(int uid);
}

package dev.leonlatsch.oliviabackend.repository;

import dev.leonlatsch.oliviabackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to access the user table
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Find a user by its email address.
     *
     * @param email
     * @return A user
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by its username.
     *
     * @param username
     * @return A user
     */
    Optional<User> findByUsername(String username);

    /**
     * Find the top 100 users matching a search query excluding the searching one.
     *
     * @param uid The uid of the excluded user.
     * @param username The username search query.
     * @return A {@link List} of users
     */
    List<User> findTop100ByUidNotAndUsernameContaining(String uid, String username);

    /**
     * Find a public key of a user by its uid.
     *
     * @param uid
     * @return A user with just its public key.
     */
    Optional<User> findPublicKeyByUid(String uid);

    /**
     * Find a uid in the table.
     * Used for safe generation.
     *
     * @param uid
     * @return A user with just its uid if it exists.
     */
    Optional<User> findUidByUid(String uid);
}

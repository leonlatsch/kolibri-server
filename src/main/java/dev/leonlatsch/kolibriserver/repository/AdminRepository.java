package dev.leonlatsch.kolibriserver.repository;

import dev.leonlatsch.kolibriserver.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository to access the admin table
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    /**
     * Find a admin by its token
     *
     * @param token
     * @return
     */
    Optional<Admin> findByToken(String token);
}

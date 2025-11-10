package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    
    @Query("SELECT DISTINCT u FROM AppUser u " +
           "LEFT JOIN FETCH u.roles " +
           "LEFT JOIN FETCH u.persona p " +
           "LEFT JOIN FETCH p.comentario " +
           "WHERE u.estadoVerificacion = :estado")
    List<AppUser> findByEstadoVerificacion(String estado);
    
    Optional<AppUser> findByPersonaId(Long personaId);
}
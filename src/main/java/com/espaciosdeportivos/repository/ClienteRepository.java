package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    boolean existsById(Long id);
    List<Cliente> findByEstadoTrue();
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    boolean existsByEmail(String email);
    
    Optional<Cliente> findByEmail(String email);
    
    // Native queries para el servicio de aprobación
    @Modifying
    @Query(value = "INSERT INTO cliente (id_persona, categoria, estado) " +
                   "VALUES (:id, :categoria, true) " +
                   "ON CONFLICT (id_persona) DO NOTHING", 
           nativeQuery = true)
    void crearClienteSiNoExiste(@Param("id") Long id, @Param("categoria") String categoria);
    
    // Método adicional para contar por email (útil para validaciones)
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.email = :email")
    boolean existsByEmailCustom(@Param("email") String email);
}
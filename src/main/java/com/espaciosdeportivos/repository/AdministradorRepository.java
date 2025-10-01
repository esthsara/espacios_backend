package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    List<Administrador> findByNombreContainingIgnoreCase(String nombre);
    boolean existsById(Long id);
    List<Administrador> findByEstadoTrue();
    List<Administrador> findByFechaNacimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT a FROM Administrador a " +
           "WHERE (:nombre IS NULL OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND (:aPaterno IS NULL OR LOWER(a.aPaterno) LIKE LOWER(CONCAT('%', :aPaterno, '%'))) " +
           "AND (:aMaterno IS NULL OR LOWER(a.aMaterno) LIKE LOWER(CONCAT('%', :aMaterno, '%')))")
    List<Administrador> buscarPorNombreApellidos(@Param("nombre") String nombre,
                                                 @Param("aPaterno") String aPaterno,
                                                 @Param("aMaterno") String aMaterno);
}

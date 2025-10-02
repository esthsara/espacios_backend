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
       "AND (:apellidoPaterno IS NULL OR LOWER(a.apellidoPaterno) LIKE LOWER(CONCAT('%', :apellidoPaterno, '%'))) " +
       "AND (:apellidoMaterno IS NULL OR LOWER(a.apellidoMaterno) LIKE LOWER(CONCAT('%', :apellidoMaterno, '%')))")
       List<Administrador> buscarPorNombreApellidos(@Param("nombre") String nombre,
                                             @Param("apellidoPaterno") String apellidoPaterno,
                                             @Param("apellidoMaterno") String apellidoMaterno);

}

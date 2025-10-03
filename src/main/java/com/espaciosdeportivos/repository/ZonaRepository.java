package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Long> {

    // Solo activos
    List<Zona> findByEstadoTrue();

    // Activo por id
    Optional<Zona> findByIdZonaAndEstadoTrue(Long idZona);

    List<Zona> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT z FROM Zona z WHERE LOWER(z.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Zona> buscarPorNombre(String nombre);
    

}



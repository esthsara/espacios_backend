package com.espaciosdeportivos.repository;


import com.espaciosdeportivos.model.AreaDeportiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaDeportivaRepository extends JpaRepository<AreaDeportiva, Long> {

    // Solo activos
    List<AreaDeportiva> findByEstadoTrue();

    //boolean existsById(Long id);

    // Activo por id
    Optional<AreaDeportiva> findByIdAreaDeportivaAndEstadoTrue(Long idAreaDeportiva);
    
    // Por zona (solo activos)
    //List<AreaDeportiva> findByZona_IdZonaAndEstadoTrue(Long idZona);

    // (Opcional) Por administrador
    //List<AreaDeportiva> findByAdministrador_IdPersonaAndEstadoTrue(Long id);

    // (Opcional) Unicidad por nombre dentro de la misma zona
    //boolean existsByNombreAreaIgnoreCaseAndZona_IdZona(String nombreArea, Long idZona);

    @Query("SELECT a FROM AreaDeportiva a WHERE LOWER(a.nombreArea) LIKE LOWER(CONCAT('%', :nombre, '%')) ")
    List<AreaDeportiva> buscarPorNombre(@Param("nombre") String nombre);

    Optional<AreaDeportiva> findByAdministrador_Id(Long Id);


}

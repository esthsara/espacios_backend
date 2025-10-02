package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CanchaRepository extends JpaRepository<Cancha, Long> {

    // Solo activas (soft delete)
    List<Cancha> findByEstadoboolTrue();

    // Activa por id
    Optional<Cancha> findByIdCanchaAndEstadoboolTrue(Long idCancha);

    // Por área deportiva (solo activas)
    //List<Cancha> findByAreaDeportiva_IdAreaDeportivaAndEstadoboolTrue(Long idAreaDeportiva);

    @Query("SELECT a FROM cancha a WHERE LOWER(a.nombreArea) LIKE LOWER(CONCAT('%', :nombre, '%')) ")
    List<Cancha> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT c FROM cancha c WHERE " +
            "(:horaInicio IS NULL OR c.horaInicio >= :horaInicio) AND " +
            "(:horaFin IS NULL OR c.horaFin <= :horaFin) AND " +
            "(:costo IS NULL OR c.costoHora <= :costo) AND " +
            "(:capacidad IS NULL OR c.capacidad >= :capacidad) AND " +
            "(:tamano IS NULL OR LOWER(c.tamano) = LOWER(:tamano)) AND " +
            "(:iluminacion IS NULL OR LOWER(c.iluminacion) = LOWER(:iluminacion)) AND " +
            "(:cubierta IS NULL OR LOWER(c.cubierta) = LOWER(:cubierta)) AND " +
            "c.estadobool = true")
    List<Cancha> buscarFiltros(@Param("horaInicio") java.time.LocalTime horaInicio,
                               @Param("horaFin") java.time.LocalTime horaFin,
                               @Param("costo") Double costo,
                               @Param("capacidad") Integer capacidad,
                               @Param("tamano") String tamano,
                               @Param("iluminacion") String iluminacion,
                               @Param("cubierta") String cubierta);

    // (Opcional) Unicidad de nombre dentro del área
   // boolean existsByNombreIgnoreCaseAndAreaDeportiva_IdAreaDeportiva(String nombre, Long idAreaDeportiva);
}

package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Equipamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipamientoRepository extends JpaRepository<Equipamiento, Long> {

    // Solo activos
    List<Equipamiento> findByEstadoboolTrue();

    // Activo por id
    Optional<Equipamiento> findByIdEquipamientoAndEstadoboolTrue(Long idEquipamiento);

    // Unicidad por nombre
    boolean existsByNombreEquipamientoIgnoreCase(String nombre);

    // (Opcional) por estado textual
    List<Equipamiento> findByEstadoAndEstadoboolTrue(String estado);
}

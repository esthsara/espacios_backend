package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Equipamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipamientoRepository extends JpaRepository<Equipamiento, Long> {

    // Solo activos
    List<Equipamiento> findByEstadoTrue();

    // Activo por id
    Optional<Equipamiento> findByIdEquipamientoAndEstadoTrue(Long idEquipamiento);

    // Unicidad por nombre
    boolean existsByNombreEquipamientoIgnoreCase(String nombre);

    // (Opcional) por estado textual
    //List<Equipamiento> findByEstadoAndEstadoTrue(Boolean estado);

    //List<Equipamiento> findByCanchaIdCancha(Long idCancha);
    
}

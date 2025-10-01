package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Macrodistrito;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MacrodistritoRepository extends JpaRepository<Macrodistrito, Long> {

    // Solo activos
    List<Macrodistrito> findByEstadoTrue();

    // Activo por id
    Optional<Macrodistrito> findById(Long idMacrodistrito);

    //Unicidad por nombre (opcionalmente úsalo si quieres reforzar) yes
    //boolean existsByNombreIgnoreCase(String nombre);

    //Optional<Macrodistrito> findByNombreIgnoreCase(String nombre);//yes

    boolean existsById(Long id);
}


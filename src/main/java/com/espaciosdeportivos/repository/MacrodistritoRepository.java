package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Macrodistrito;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT m FROM Macrodistrito m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Macrodistrito> buscarPorNombre(@Param("nombre") String nombre);
}


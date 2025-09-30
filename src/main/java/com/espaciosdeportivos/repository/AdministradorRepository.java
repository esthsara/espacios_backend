package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    List<Administrador> findByNombreContainingIgnoreCase(String nombre);
    //relacion con area deportiva
    boolean existsById(Long id);
}

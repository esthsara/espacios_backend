package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.UsuarioControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioControlRepository extends JpaRepository<UsuarioControl, Long> {
    List<UsuarioControl> findByNombreContainingIgnoreCase(String nombre);
}

package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.UsuarioControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioControlRepository extends JpaRepository<UsuarioControl, Long> {

    // Buscar por coincidencia en el nombre (ignora mayúsculas/minúsculas)
    List<UsuarioControl> findByNombreContainingIgnoreCase(String nombre);

    // Verificar existencia por ID
    boolean existsById(Long id);

}

package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar clientes por coincidencia en el nombre (ignora mayúsculas/minúsculas)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    // Verificar existencia por ID
    boolean existsById(Long id);

    // Listar solo clientes activos (estado = true en Persona)
    List<Cliente> findByEstadoTrue();


}

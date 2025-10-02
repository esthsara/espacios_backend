package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.AdministradorDTO;
//import com.espaciosdeportivos.model.Administrador;

import java.time.LocalDate;
import java.util.List;

public interface AdministradorService {
    // Listar todos los administradores activos
    List<AdministradorDTO> obtenerTodoslosAdministradores();

    // Listar todos, incluyendo desactivados 
    List<AdministradorDTO> listarTodos();

    // Buscar por ID
    AdministradorDTO obtenerAdministradorPorId(Long id);

    // Buscar por nombre
    List<AdministradorDTO> buscarPorNombre(String nombre);

    // Crear nuevo administrador
    AdministradorDTO crearAdministrador(AdministradorDTO dto);

    // Editar administrador existente
    AdministradorDTO actualizarAdministrador(Long id,AdministradorDTO dto);

    // Eliminar físicamente (solo si realmente quieres borrar)
    void eliminarAdministrador(Long id);

    // Desactivar (eliminación lógica)
    AdministradorDTO cambiarEstado(Long id, Boolean nuevoEstado);

    //buscar en un rango de una fecha
    List<AdministradorDTO> buscarPorRangoFecha(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar por nombre y apellidos (nombre, aPaterno, aMaterno)
    List<AdministradorDTO> buscarPorNombreApellidos(String nombre, String aPaterno, String aMaterno);

}

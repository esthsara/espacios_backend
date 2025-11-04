package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.UsuarioControlDTO;
import java.util.List;

public interface UsuarioControlService {

    // Listar todos los usuarios de control activos
    List<UsuarioControlDTO> obtenerTodosLosUsuariosControl();

    // Listar todos, incluyendo desactivados
    List<UsuarioControlDTO> listarTodos();

    // Buscar por ID
    UsuarioControlDTO obtenerUsuarioControlPorId(Long id);

    // Buscar por nombre
    List<UsuarioControlDTO> buscarPorNombre(String nombre);

    // Crear nuevo usuario de control
    UsuarioControlDTO crearUsuarioControl(UsuarioControlDTO dto);

    // Editar usuario existente
    UsuarioControlDTO actualizarUsuarioControl(Long id, UsuarioControlDTO dto);

    // Eliminar físicamente (solo si realmente quieres borrar)
    void eliminarUsuarioControl(Long id);

    // Cambiar estado (activación/desactivación lógica)
    UsuarioControlDTO cambiarEstado(Long id, Boolean nuevoEstado);

    // Relación: canchas que supervisa
    //List<CanchaDTO> obtenerCanchasSupervisadas(Long usuarioControlId)
}

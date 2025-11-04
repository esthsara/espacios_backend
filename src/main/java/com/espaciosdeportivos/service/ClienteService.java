package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.CancelacionDTO;
import com.espaciosdeportivos.dto.ClienteDTO;
import com.espaciosdeportivos.dto.PagoDTO;
import com.espaciosdeportivos.dto.QrDTO;
import com.espaciosdeportivos.dto.ReservaDTO;

import java.util.List;

public interface ClienteService {
    // Listar todos los clientes activos
    List<ClienteDTO> obtenerTodoslosClientes();

    // Listar todos, incluyendo desactivados
    List<ClienteDTO> listarTodos();

    // Buscar por ID
    ClienteDTO obtenerClientePorId(Long id);

    // Buscar por nombre
    List<ClienteDTO> buscarPorNombre(String nombre);

    // Crear nuevo cliente
    ClienteDTO crearCliente(ClienteDTO dto);

    // Editar cliente existente
    ClienteDTO actualizarCliente(Long id, ClienteDTO dto);

    // Eliminar físicamente (solo si realmente quieres borrar)
    void eliminarCliente(Long id);

    // Cambiar estado (activación/desactivación lógica)
    ClienteDTO cambiarEstado(Long id, Boolean nuevoEstado);

        // Datos relacionados (¡aquí sí!)
    /*List<ReservaDTO> obtenerReservasDeCliente(Long clienteId);
    List<PagoDTO> obtenerPagosDeCliente(Long clienteId);
    List<QrDTO> obtenerQrsDeCliente(Long clienteId);
    List<CancelacionDTO> obtenerCancelacionesDeCliente(Long clienteId);*/
}

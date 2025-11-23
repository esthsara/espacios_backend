package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.CancelacionDTO;
import com.espaciosdeportivos.dto.ClienteDTO;
import com.espaciosdeportivos.dto.PagoDTO;
import com.espaciosdeportivos.dto.QrDTO;
import com.espaciosdeportivos.dto.ReservaDTO;

import java.util.List;

public interface ClienteService {
    List<ClienteDTO> obtenerTodoslosClientes();
    List<ClienteDTO> listarTodos();
    ClienteDTO obtenerClientePorId(Long id);
    List<ClienteDTO> buscarPorNombre(String nombre);
    ClienteDTO crearCliente(ClienteDTO dto);
    ClienteDTO actualizarCliente(Long id, ClienteDTO dto);
    void eliminarCliente(Long id);
    ClienteDTO cambiarEstado(Long id, Boolean nuevoEstado);

        // Datos relacionados (¡aquí sí!)
    /*List<ReservaDTO> obtenerReservasDeCliente(Long clienteId);
    List<PagoDTO> obtenerPagosDeCliente(Long clienteId);
    List<QrDTO> obtenerQrsDeCliente(Long clienteId);
    List<CancelacionDTO> obtenerCancelacionesDeCliente(Long clienteId);*/
}
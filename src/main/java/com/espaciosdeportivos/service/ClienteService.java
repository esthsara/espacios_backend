package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ClienteDTO;
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
}
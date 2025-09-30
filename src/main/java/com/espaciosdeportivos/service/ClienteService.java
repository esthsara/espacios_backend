package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ClienteDTO;
import java.util.List;

public interface ClienteService {
    ClienteDTO crearCliente(ClienteDTO clienteDTO);
    ClienteDTO obtenerPorId(Long id);
    List<ClienteDTO> listarTodos();
    ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO);
    void eliminarCliente(Long id);
    List<ClienteDTO> buscarPorNombre(String nombre);
}

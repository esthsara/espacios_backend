package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.AdministradorDTO;
import java.util.List;

public interface AdministradorService {
    AdministradorDTO crearAdministrador(AdministradorDTO dto);
    AdministradorDTO obtenerPorId(Long id);
    List<AdministradorDTO> listarTodos();
    AdministradorDTO actualizarAdministrador(Long id, AdministradorDTO dto);
    void eliminarAdministrador(Long id);
    List<AdministradorDTO> buscarPorNombre(String nombre);
}

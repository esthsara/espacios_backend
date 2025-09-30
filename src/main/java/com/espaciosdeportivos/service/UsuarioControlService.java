package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.UsuarioControlDTO;
import java.util.List;

public interface UsuarioControlService {
    UsuarioControlDTO crear(UsuarioControlDTO dto);
    UsuarioControlDTO actualizar(Long id, UsuarioControlDTO dto);
    void eliminar(Long id);
    UsuarioControlDTO obtenerPorId(Long id);
    List<UsuarioControlDTO> listar();
    List<UsuarioControlDTO> buscarPorNombre(String nombre);
}

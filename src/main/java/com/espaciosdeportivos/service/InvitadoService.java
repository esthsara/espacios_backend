package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.InvitadoDTO;
import java.util.List;

public interface InvitadoService {
    InvitadoDTO crearInvitado(InvitadoDTO dto);
    InvitadoDTO obtenerPorId(Long id);
    List<InvitadoDTO> listarTodos();
    InvitadoDTO actualizarInvitado(Long id, InvitadoDTO dto);
    void eliminarInvitado(Long id);
    List<InvitadoDTO> buscarPorNombre(String nombre);
}

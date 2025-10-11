package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ComentarioDTO;
import com.espaciosdeportivos.model.Comentario;

import java.util.List;

public interface IComentarioService {

    List<ComentarioDTO> obtenerTodosLosComentarios();

    ComentarioDTO obtenerComentarioPorId(Long id);

    ComentarioDTO crearComentario(ComentarioDTO comentarioDTO);

    ComentarioDTO actualizarComentario(Long id, ComentarioDTO comentarioDTO);

    ComentarioDTO eliminarComentario(Long id); // baja logica T O F

    Comentario obtenerComentarioConBloqueo(Long id);

    void eliminarComentarioFisicamente(Long id);
}
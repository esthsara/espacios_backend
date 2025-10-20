package com.espaciosdeportivos.service;

import jakarta.validation.Valid;
import com.espaciosdeportivos.dto.AreaDeportivaDTO;
import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.model.AreaDeportiva;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IAreaDeportivaService {
    // Listar todas las áreas deportivas activas
    List<AreaDeportivaDTO> obtenerTodasLasAreasDeportivas();

    // Listar todas las áreas deportivas (incluso inactivas)
    List<AreaDeportivaDTO> listarTodos();

    // Obtener área deportiva por ID
    AreaDeportivaDTO obtenerAreaDeportivaPorId(Long idAreaDeportiva);
    
    // crear área deportiva
    AreaDeportivaDTO crearAreaDeportiva(@Valid AreaDeportivaDTO areaDeportivaDTO);
    
    // actualizar área deportiva
    AreaDeportivaDTO actualizarAreaDeportiva(Long idAreaDeportiva, @Valid AreaDeportivaDTO areaDeportivaDTO);
    
    //eliminar área fisicamente
    void eliminarAreaDeportivaFisicamente(Long idAreaDeportiva);

    //eliiminar área deportiva (eliminación lógica)
    AreaDeportivaDTO eliminarAreaDeportiva(Long idAreaDeportiva, Boolean nuevoEstado); // baja lógica (estado=false)
    
    // buscar área deportiva por nombre
    List<AreaDeportivaDTO> buscarPorNombre(String nombre);
    
    // obtener área deportiva con bloqueo optimista
    AreaDeportiva obtenerAreaDeportivaConBloqueo(Long idAreaDeportiva); // para uso interno con bloqueo

    // Gestión de imágenes específica
    AreaDeportivaDTO agregarImagenes(Long idCancha, List<MultipartFile> archivosImagenes);
    AreaDeportivaDTO eliminarImagen(Long idCancha, Long idImagenRelacion);
    AreaDeportivaDTO reordenarImagenes(Long idCancha, List<Long> idsImagenesOrden);

}

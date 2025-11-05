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
    
    //eliminar área fisicamente revisar si se borraria todo o solo desactivamos o se quedan solitas las canchas como perdidas 
    //la solucion que yo veo es que las relaciones con oras entidades se desactiven ponte que si elimino aredeportiva se eliminara las canchas y las relaciones con las demas tablitas ponte que hay reserva relacinado dcon cacnha y diciplin aesto se hara de uan manera mas sencialla
    void eliminarAreaDeportivaFisicamente(Long idAreaDeportiva);

    //eliiminar área deportiva (eliminación lógica)
    AreaDeportivaDTO eliminarAreaDeportiva(Long idAreaDeportiva, Boolean nuevoEstado); // baja lógica (estado=false)
    
    // buscar área deportiva por nombre
    List<AreaDeportivaDTO> buscarPorNombre(String nombre);
    
    //Listar Areasd-deportivas por zona/
    //List<AreaDeportivaDTO> listarPorZona(Long idZona);

    //Listar areas deportivas por macrodistrito
    //List<AreaDeportivaDTO> listarPorMacrodistrito(Long idMacrodistrito);

    //sacar el promedio de calificaciones de un area deportiva sumando y promediando todas las calificaiones de sus canchas
    //Double obtenerPromedioCalificacionesPorAreaDeportiva(Long idAreaDeportiva);

    
    AreaDeportiva obtenerAreaDeportivaConBloqueo(Long idAreaDeportiva); // para uso interno con bloqueo

    //MI_AREA k
    AreaDeportivaDTO obtenerPorAdminId(Long Id);

    //MI_AREA k actualizar por adminId
    AreaDeportivaDTO actualizarPorAdminId(Long adminId, AreaDeportivaDTO dto);




    // Gestión de imágenes específica
    AreaDeportivaDTO agregarImagenes(Long idCancha, List<MultipartFile> archivosImagenes);
    AreaDeportivaDTO eliminarImagen(Long idCancha, Long idImagenRelacion);
    AreaDeportivaDTO reordenarImagenes(Long idCancha, List<Long> idsImagenesOrden);

}

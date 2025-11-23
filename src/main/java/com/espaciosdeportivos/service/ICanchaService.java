package com.espaciosdeportivos.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.dto.ReservaDTO;
import com.espaciosdeportivos.model.Cancha;


public interface ICanchaService {
    //listar todas las canchas activas
    List<CanchaDTO> obtenerTodasLasCanchas();
    //listar todos
    List<CanchaDTO> ListarTodos();

    //Buscar por Id
    CanchaDTO obtenerCanchaPorId(Long idCancha);
    //Buscar por Nombre
    List<CanchaDTO> buscarPorNombre(String nombre);
    //crear nueva cancha
    CanchaDTO crearCancha(@Valid CanchaDTO canchaDTO);
    //Actualizar cancha existente
    CanchaDTO actualizarCancha(Long idCancha, @Valid CanchaDTO canchaDTO);
    //Eliminar físicamente (solo si realmente quieres borrar)
    void eliminarCanchaFisicamente(Long idCancha);
    //Desactivar (eliminación lógica)
    CanchaDTO eliminarCancha(Long idCancha, Boolean nuevoEstado); 
    
    //cancha con bloqueo optimista
    Cancha obtenerCanchaConBloqueo(Long idCancha); // para uso interno con bloqueo
    // uso interno

    //Cancha en un rango de fecha
    List<CanchaDTO> BuscarConFiltros(LocalTime horaInicio, LocalTime horaFin, Double costo, Integer capacidad,
                                            String tamano, String iluminacion, String cubierta);
                                            
    //listar canchas por area deportiva
    //List<CanchaDTO> listarPorAreaDeportiva(Long idAreaDeportiva);

    //listar canchas por zona 
    //List<CanchaDTO> listarPorZona(Long idZona);

    //Listar canchas por macrodistrito
    //List<CanchaDTO> listarPorMacrodistrito(Long idMacrodistrito);

    //listar diciplinas que tiene una cancha 
    List<DisciplinaDTO> obtenerDiciplinasPorCancha(Long idCancha);

    //Listar Resevas de una cancha
    //List<ReservaDTO> obtenerReservasPorCancha(Long idCancha);

    //Sacar el promedio de calificaciones de una cancha
    //Double obtenerPromedioCalificacionesPorCancha(Long idCancha);

    //listar equipamiento por cancha
    List<EquipamientoDTO> obtenerEquipamientoPorCancha(Long idCancha);

    //List<ReservaDTO> obtenerReservaPorCancha(Long idCancha);

    //ADMIN K - Obtener canchas por área deportiva
    List<CanchaDTO> obtenerCanchasPorArea(Long idArea);
    
    // Gestión de imágenes específica
    CanchaDTO agregarImagenes(Long idCancha, List<MultipartFile> archivosImagenes);
    CanchaDTO eliminarImagen(Long idCancha, Long idImagenRelacion);
    CanchaDTO reordenarImagenes(Long idCancha, List<Long> idsImagenesOrden);

    
}

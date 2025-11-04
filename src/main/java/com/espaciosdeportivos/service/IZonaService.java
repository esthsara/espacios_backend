package com.espaciosdeportivos.service;

import java.util.List;
import jakarta.validation.Valid;
import com.espaciosdeportivos.dto.ZonaDTO;
import com.espaciosdeportivos.model.Zona;

public interface IZonaService {

    //Listar todas las zonas activas
    List<ZonaDTO> obtenerTodasLasZonas();

    //Listar  todos
    List<ZonaDTO> ListarTodos();

    //Buscar por Id
    ZonaDTO obtenerZonaPorId(Long idZona);

    //Buscar por Nombre
    List<ZonaDTO> buscarPorNombre(String nombre);

    //Crear nueva zona
    ZonaDTO crearZona(@Valid ZonaDTO zonaDTO);

    //Actualizar zona existente
    ZonaDTO actualizarZona(Long idZona, @Valid ZonaDTO zonaDTO);

    //Eliminar físicamente (solo si realmente quieres borrar)
    void eliminarZonaFisicamente(Long idZona); 

    //Desactivar (eliminación lógica)
    ZonaDTO eliminarZona(Long idZona, Boolean nuevoEstado);

    // Zona con bloqueo optimista
    Zona obtenerZonaConBloqueo(Long idZona);

    //obtner todas las zonas de un macrodistrito
    //List<ZonaDTO> obtenerZonasPorMacrodistritoId(Long idMacrodistrito);

    //buscar zonas por macrodistrito y nombre
    //List<ZonaDTO> buscarZonasPorMacrodistritoYNombre(Long idMacrodistrito, String nombre);


}

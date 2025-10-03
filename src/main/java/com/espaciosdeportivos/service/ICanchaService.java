package com.espaciosdeportivos.service;

import java.time.LocalTime;
import java.util.List;
import jakarta.validation.Valid;
import com.espaciosdeportivos.dto.CanchaDTO;
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
    

}

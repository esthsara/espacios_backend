package com.espaciosdeportivos.service;

import java.util.List;
import jakarta.validation.Valid;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.model.Equipamiento;

public interface IEquipamientoService {
    //Listar todos los equipamientos activos
    List<EquipamientoDTO> obtenerTodosLosEquipamientos();   
    //listar los equipamiento por id
    EquipamientoDTO obtenerEquipamientoPorId(Long idEquipamiento); 
    //crear equipamiento
    EquipamientoDTO crearEquipamiento(@Valid EquipamientoDTO equipamientoDTO);
    //actualizar equipamiento
    EquipamientoDTO actualizarEquipamiento(Long idEquipamiento, @Valid EquipamientoDTO equipamientoDTO);
    //eliminar equipamiento (eliminación lógica)
    EquipamientoDTO eliminarEquipamiento(Long idEquipamiento); 
    //sa
    Equipamiento obtenerEquipamientoConBloqueo(Long idEquipamiento); // para uso interno con bloqueo
    //eliminar equipamiento fisicamente
    void eliminarEquipamientoFisicamente(Long idEquipamiento);                 // uso interno   
}

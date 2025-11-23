package com.espaciosdeportivos.service;

import java.util.List;
import jakarta.validation.Valid;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.model.Equipamiento;

public interface IEquipamientoService {
    List<EquipamientoDTO> obtenerTodosLosEquipamientos();                      // activos
    EquipamientoDTO obtenerEquipamientoPorId(Long idEquipamiento);            // activo
    EquipamientoDTO crearEquipamiento(@Valid EquipamientoDTO equipamientoDTO);
    EquipamientoDTO actualizarEquipamiento(Long idEquipamiento, @Valid EquipamientoDTO equipamientoDTO);
    EquipamientoDTO eliminarEquipamiento(Long idEquipamiento);                // baja lógica
    Equipamiento obtenerEquipamientoConBloqueo(Long idEquipamiento); // para uso interno con bloqueo
    void eliminarEquipamientoFisicamente(Long idEquipamiento);                 // uso interno   
}

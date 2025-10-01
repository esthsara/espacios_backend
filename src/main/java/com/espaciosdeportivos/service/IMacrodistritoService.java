package com.espaciosdeportivos.service;

import java.util.List;

import com.espaciosdeportivos.dto.MacrodistritoDTO;
import com.espaciosdeportivos.model.Macrodistrito;

import jakarta.validation.Valid;

public interface IMacrodistritoService {
    List<MacrodistritoDTO> obtenerTodosLosMacrodistritos();
    MacrodistritoDTO obtenerMacrodistritoPorId(Long idMacrodistrito);
    MacrodistritoDTO crearMacrodistrito(@Valid MacrodistritoDTO macrodistritoDTO);
    MacrodistritoDTO actualizarMacrodistrito(Long idMacrodistrito, @Valid MacrodistritoDTO macrodistritoDTO);
    MacrodistritoDTO eliminarMacrodistrito(Long idMacrodistrito);
    Macrodistrito obtenerMacrodistritoConBloqueo(Long idMacrodistrito);
    void eliminarMacrodistritoFisicamente(Long idMacrodistrito);
}

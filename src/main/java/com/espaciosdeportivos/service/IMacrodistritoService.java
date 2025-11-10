package com.espaciosdeportivos.service;

import java.util.List;

import com.espaciosdeportivos.dto.MacrodistritoDTO;
import com.espaciosdeportivos.model.Macrodistrito;

import jakarta.validation.Valid;

public interface IMacrodistritoService {

        //Listar todos los macroditritos incluido los inactivos
    List<MacrodistritoDTO> ListarTodos();

    //Listar todos los macrodistritos activos
    List<MacrodistritoDTO> obtenerTodosLosMacrodistritos();

    //obtner macrodistrito por id
    MacrodistritoDTO obtenerMacrodistritoPorId(Long idMacrodistrito);

    //crear macrodistrito
    MacrodistritoDTO crearMacrodistrito(@Valid MacrodistritoDTO macrodistritoDTO);
    
    //actualizar macrodistrito
    MacrodistritoDTO actualizarMacrodistrito(Long idMacrodistrito, @Valid MacrodistritoDTO macrodistritoDTO);
    
    //eliminar macrodistrito fisicamente
    void eliminarMacrodistritoFisicamente(Long idMacrodistrito);

    //eliminar macrodistrito (eliminacion logica) es decir desactivar
    MacrodistritoDTO eliminarMacrodistrito(Long idMacrodistrito, Boolean nuevoEstado);

    //buscar macrodistrito por nombre
    List<MacrodistritoDTO> buscarPorNombre(String nombre);

    //obtener macrodistrito con bloqueo optimista
    Macrodistrito obtenerMacrodistritoConBloqueo(Long idMacrodistrito);
    
}

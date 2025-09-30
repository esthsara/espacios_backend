package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.PersonaDTO;
import java.util.List;

public interface PersonaService {
    PersonaDTO crear(PersonaDTO personaDTO);
    PersonaDTO actualizar(Long id, PersonaDTO personaDTO);
    void eliminar(Long id);
    PersonaDTO obtenerPorId(Long id);
    List<PersonaDTO> listar();
    List<PersonaDTO> buscarPorNombre(String nombre);
}

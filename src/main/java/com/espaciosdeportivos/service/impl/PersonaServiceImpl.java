package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.PersonaDTO;
import com.espaciosdeportivos.model.Persona;
import com.espaciosdeportivos.repository.PersonaRepository;
import com.espaciosdeportivos.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    private PersonaDTO mapToDTO(Persona persona){
        return PersonaDTO.builder()
                .idPersona(persona.getIdPersona())
                .nombre(persona.getNombre())
                .aPaterno(persona.getAPaterno())
                .aMaterno(persona.getAMaterno())
                .fechaNacimiento(persona.getFechaNacimiento())
                .telefono(persona.getTelefono())
                .email(persona.getEmail())
                //.ci(persona.getCi())
                .build();
    }

    private Persona mapToEntity(PersonaDTO dto){
        return Persona.builder()
                .idPersona(dto.getIdPersona())
                .nombre(dto.getNombre())
                .aPaterno(dto.getAPaterno())
                .aMaterno(dto.getAMaterno())
                .fechaNacimiento(dto.getFechaNacimiento())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                //.ci(dto.getCi())
                .build();
    }

    @Override
    public PersonaDTO crear(PersonaDTO personaDTO) {
        Persona persona = mapToEntity(personaDTO);
        return mapToDTO(personaRepository.save(persona));
    }

    @Override
    public PersonaDTO actualizar(Long id, PersonaDTO personaDTO) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        persona.setNombre(personaDTO.getNombre());
        persona.setAPaterno(personaDTO.getAPaterno());
        persona.setAMaterno(personaDTO.getAMaterno());
        persona.setFechaNacimiento(personaDTO.getFechaNacimiento());
        persona.setTelefono(personaDTO.getTelefono());
        persona.setEmail(personaDTO.getEmail());
        //persona.setCi(personaDTO.getCi());
        return mapToDTO(personaRepository.save(persona));
    }

    @Override
    public void eliminar(Long id) {
        personaRepository.deleteById(id);
    }

    @Override
    public PersonaDTO obtenerPorId(Long id) {
        return personaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
    }

    @Override
    public List<PersonaDTO> listar() {
        return personaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonaDTO> buscarPorNombre(String nombre) {
        return personaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}

package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.sepracticaDTO;
import com.espaciosdeportivos.model.*;
import com.espaciosdeportivos.repository.*;
import com.espaciosdeportivos.service.IsepracticaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class sepracticaServiceImpl implements IsepracticaService {

    private final sepracticaRepository sepracticaRepository;
    private final CanchaRepository canchaRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Override
    @Transactional
    public sepracticaDTO asociarDisciplinaACancha(sepracticaDTO dto) {
        Cancha cancha = canchaRepository.findById(dto.getIdCancha())
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + dto.getIdCancha()));

        Disciplina disciplina = disciplinaRepository.findById(dto.getIdDisciplina())
                .orElseThrow(() -> new EntityNotFoundException("Disciplina no encontrada con ID: " + dto.getIdDisciplina()));

        sepracticaId id = new sepracticaId(dto.getIdCancha(), dto.getIdDisciplina());

        sepractica entity = sepracticaRepository.findById(id).orElseGet(() ->
                sepractica.builder()
                        .id(id)
                        .cancha(cancha)
                        .disciplina(disciplina)
                        .nivelDificultad(dto.getNivelDificultad())
                        .recomendaciones(dto.getRecomendaciones())
                        .build()
        );
        
        entity.setNivelDificultad(dto.getNivelDificultad());
        entity.setRecomendaciones(dto.getRecomendaciones());

        sepractica saved = sepracticaRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public sepracticaDTO obtenerDisciplinaDeCancha(Long idCancha, Long idDisciplina) {
        sepracticaId id = new sepracticaId(idCancha, idDisciplina);

        sepractica association = sepracticaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Relación no encontrada: Cancha ID " + idCancha + " y Disciplina ID " + idDisciplina
                ));
        return convertToDTO(association);
    }

    @Override
    @Transactional(readOnly = true)
    public List<sepracticaDTO> obtenerDisciplinasPorCancha(Long idCancha) {
        canchaRepository.findById(idCancha)
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + idCancha));

        return sepracticaRepository.findById_IdCancha(idCancha).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void desasociarDisciplinaDeCancha(Long idCancha, Long idDisciplina) {
        if (!sepracticaRepository.existsById_IdCanchaAndId_IdDisciplina(idCancha, idDisciplina)) {
            throw new EntityNotFoundException(
                "Relación no encontrada para eliminar: Cancha ID " + idCancha + " y Disciplina ID " + idDisciplina
            );
        }
        sepracticaRepository.deleteById_IdCanchaAndId_IdDisciplina(idCancha, idDisciplina);
    }

    @Override
    @Transactional(readOnly = true)
    public List<sepracticaDTO> listarPorIdCancha(Long idCancha) {
        return sepracticaRepository.findById_IdCancha(idCancha).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<sepracticaDTO> listarPorIdDisciplina(Long idDisciplina) {
        return sepracticaRepository.findByDisciplina_IdDisciplina(idDisciplina).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private sepracticaDTO convertToDTO(sepractica entity) {
        return sepracticaDTO.builder()
                .idCancha(entity.getCancha().getIdCancha())
                .idDisciplina(entity.getDisciplina().getIdDisciplina())
                .nivelDificultad(entity.getNivelDificultad())
                .recomendaciones(entity.getRecomendaciones())
                .build();
    }
}
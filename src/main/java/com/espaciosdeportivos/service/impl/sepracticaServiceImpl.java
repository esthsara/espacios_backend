package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.SepracticaDTO;
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
    public SepracticaDTO asociarDisciplinaACancha(SepracticaDTO dto) {
        Cancha cancha = canchaRepository.findById(dto.getIdCancha())
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + dto.getIdCancha()));

        Disciplina disciplina = disciplinaRepository.findById(dto.getIdDisciplina())
                .orElseThrow(() -> new EntityNotFoundException("Disciplina no encontrada con ID: " + dto.getIdDisciplina()));

        SepracticaId id = new SepracticaId(dto.getIdCancha(), dto.getIdDisciplina());

        Sepractica entity = sepracticaRepository.findById(id).orElseGet(() ->
                Sepractica.builder()
                        .id(id)
                        .cancha(cancha)
                        .disciplina(disciplina)
                        .nivelDificultad(dto.getNivelDificultad())
                        .recomendaciones(dto.getRecomendaciones())
                        .build()
        );
        
        entity.setNivelDificultad(dto.getNivelDificultad());
        entity.setRecomendaciones(dto.getRecomendaciones());

        Sepractica saved = sepracticaRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SepracticaDTO obtenerDisciplinaDeCancha(Long idCancha, Long idDisciplina) {
        SepracticaId id = new SepracticaId(idCancha, idDisciplina);

        Sepractica association = sepracticaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Relación no encontrada: Cancha ID " + idCancha + " y Disciplina ID " + idDisciplina
                ));
        return convertToDTO(association);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SepracticaDTO> obtenerDisciplinasPorCancha(Long idCancha) {
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
    public List<SepracticaDTO> listarPorIdCancha(Long idCancha) {
        return sepracticaRepository.findById_IdCancha(idCancha).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SepracticaDTO> listarPorIdDisciplina(Long idDisciplina) {
        return sepracticaRepository.findByDisciplina_IdDisciplina(idDisciplina).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SepracticaDTO convertToDTO(Sepractica entity) {
        return SepracticaDTO.builder()
                .idCancha(entity.getCancha().getIdCancha())
                .idDisciplina(entity.getDisciplina().getIdDisciplina())
                .nivelDificultad(entity.getNivelDificultad())
                .recomendaciones(entity.getRecomendaciones())
                .build();
    }
}
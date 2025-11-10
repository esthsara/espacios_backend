package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.model.Cancha;
import com.espaciosdeportivos.model.Equipamiento;

import com.espaciosdeportivos.repository.EquipamientoRepository;

import com.espaciosdeportivos.service.IEquipamientoService;
import com.espaciosdeportivos.validation.EquipamientoValidator;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EquipamientoServiceImpl implements IEquipamientoService {

    private final EquipamientoRepository equipamientoRepository;
    private final EquipamientoValidator equipamientoValidator;

   /*  @Autowired
    public EquipamientoServiceImpl(EquipamientoRepository equipamientoRepository, EquipamientoValidator equipamientoValidator) {
        this.equipamientoRepository = equipamientoRepository;
        this.equipamientoValidator = equipamientoValidator;
    }*/

    @Override
    @Transactional(readOnly = true)
    public List<EquipamientoDTO> obtenerTodosLosEquipamientos() {
        log.info("Obteniendo todas los equipamientos");
        return equipamientoRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                //.toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<EquipamientoDTO> ListarTodos() {
        log.info("Obteniendo todos los equipamientos");
        return equipamientoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                //.toList();
    }



    @Override
    @Transactional(readOnly = true)
    public EquipamientoDTO obtenerEquipamientoPorId(Long id) {
        Equipamiento eq = equipamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado con ID: " + id));
        return convertToDTO(eq);
    }

    @Override
    public EquipamientoDTO crearEquipamiento(EquipamientoDTO dto) {
        equipamientoValidator.validarEquipamiento(dto);

        // Unicidad por nombre (opcional, descomenta si quieres reforzar)
        // if (equipamientoRepository.existsByNombreequipamientoIgnoreCase(dto.getNombre())) {
        //     throw new EquipamientoValidator.BusinessException("Ya existe un equipamiento con ese nombre.");
        // }

        Equipamiento equipamiento = convertToEntity(dto);
        equipamiento.setIdEquipamiento(null);
        equipamiento.setEstado(Boolean.TRUE);
        Equipamiento guardada = equipamientoRepository.save(equipamiento);

        return convertToDTO(guardada);
    }

    @Override
    public EquipamientoDTO actualizarEquipamiento(Long id, @Valid EquipamientoDTO dto) {
        Equipamiento existente = equipamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado con ID: " + id));

        equipamientoValidator.validarEquipamiento(dto);

        // Unicidad por nombre ignorando el mismo id (opcional)
        // equipamientoRepository.findByNombreequipamientoIgnoreCase(dto.getNombre())
        //     .filter(e -> !e.getIdequipamiento().equals(id))
        //     .ifPresent(e -> { throw new EquipamientoValidator.BusinessException("Ya existe un equipamiento con ese nombre."); });

        existente.setNombreEquipamiento(dto.getNombreEquipamiento());
        existente.setTipoEquipamiento(dto.getTipoEquipamiento());
        existente.setDescripcion(dto.getDescripcion());
        //existente.setEstado(dto.getEstado());
        existente.setUrlImagen(dto.getUrlImagen());
        existente.setEstado(dto.getEstado());

        return convertToDTO(equipamientoRepository.save(existente));
    }

    @Override
    public EquipamientoDTO eliminarEquipamiento(Long id) {
        Equipamiento existente = equipamientoRepository.findByIdEquipamientoAndEstadoTrue(id)
                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado con ID: " + id));
        existente.setEstado(Boolean.FALSE); // baja lógica
        return convertToDTO(equipamientoRepository.save(existente));
    }
    @Override
    public EquipamientoDTO eliminar(Long id, Boolean nuevoEstado) {
        Equipamiento existente = equipamientoRepository.findByIdEquipamientoAndEstadoTrue(id)
                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado con ID: " + id));
        existente.setEstado(nuevoEstado); // baja lógica
        return convertToDTO(equipamientoRepository.save(existente));
    }


    @Override
    @Transactional
    public Equipamiento obtenerEquipamientoConBloqueo(Long id) {
        Equipamiento equipamiento = equipamientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamiento  no encontrado con ID: " + id));
        try {
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return equipamiento;
    }

    @Override
    @Transactional
    public void eliminarEquipamientoFisicamente(Long id) {
        Equipamiento equipamiento = equipamientoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Equipamiento no encontrada con ID: " + id));

        equipamientoRepository.delete(equipamiento);
    }

    // ---------- mapping ----------
    private EquipamientoDTO convertToDTO(Equipamiento e) {
        return EquipamientoDTO.builder()
                .idEquipamiento(e.getIdEquipamiento())
                .nombreEquipamiento(e.getNombreEquipamiento())
                .tipoEquipamiento(e.getTipoEquipamiento())
                .descripcion(e.getDescripcion())
                //.estado(e.getEstado())
                .urlImagen(e.getUrlImagen())
                .estado(e.getEstado())
                .build();
    }

    private Equipamiento convertToEntity(EquipamientoDTO d) {
        return Equipamiento.builder()
                .idEquipamiento(d.getIdEquipamiento())
                .nombreEquipamiento(d.getNombreEquipamiento())
                .tipoEquipamiento(d.getTipoEquipamiento())
                .descripcion(d.getDescripcion())
                //.estado(d.getEstado())
                .urlImagen(d.getUrlImagen())
                .estado(d.getEstado() != null ? d.getEstado() : Boolean.TRUE)
                .build();
    }
}

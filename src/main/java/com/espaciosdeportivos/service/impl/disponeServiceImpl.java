package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.DisponeDTO;
import com.espaciosdeportivos.model.*;
import com.espaciosdeportivos.repository.*;
import com.espaciosdeportivos.service.IdisponeService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DisponeServiceImpl implements IdisponeService {

    private final disponeRepository disponeRepository;
    private final CanchaRepository canchaRepository;
    private final EquipamientoRepository equipamientoRepository;

    @Autowired
    public DisponeServiceImpl(disponeRepository disponeRepository,
                              CanchaRepository canchaRepository,
                              EquipamientoRepository equipamientoRepository) {
        this.disponeRepository = disponeRepository;
        this.canchaRepository = canchaRepository;
        this.equipamientoRepository = equipamientoRepository;
    }

    @Override
    @Transactional
    public DisponeDTO asociarEquipamientoACancha(DisponeDTO dto) {
        // Validaciones simples de negocio (opcional)
        if (dto.getCantidad() == null || dto.getCantidad() < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1");
        }

        Cancha cancha = canchaRepository.findById(dto.getIdCancha())
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + dto.getIdCancha()));

        Equipamiento equipamiento = equipamientoRepository.findById(dto.getIdEquipamiento())
                .orElseThrow(() -> new EntityNotFoundException("Equipamiento no encontrado con ID: " + dto.getIdEquipamiento()));

            DisponeId id = new   DisponeId(dto.getIdCancha(), dto.getIdEquipamiento());

        // Crea o actualiza
        Dispone entity = disponeRepository.findById(id).orElseGet(() ->
                Dispone.builder()
                        .id(id)
                        .cancha(cancha)
                        .equipamiento(equipamiento)
                        .cantidad(dto.getCantidad())
                        .build()
        );
        entity.setCantidad(dto.getCantidad());

        Dispone saved = disponeRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DisponeDTO obtenerEquipamientoDeCancha(Long idCancha, Long idEquipamiento) {
            DisponeId id = new   DisponeId(idCancha, idEquipamiento);

        // Si quieres asegurar relaciones precargadas aún sin transacción activa, usa:
        // dispone association = disponeRepository.findWithGraphById(id)
        //        .orElseThrow(...)

        Dispone association = disponeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Relación no encontrada: Cancha ID " + idCancha + " y Equipamiento ID " + idEquipamiento
                ));
        return convertToDTO(association);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisponeDTO> obtenerEquipamientosPorCancha(Long idCancha) {
        canchaRepository.findById(idCancha)
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + idCancha));

        // Este método ya tiene EntityGraph en el repo, así que ok acceder a relaciones
        return disponeRepository.findById_IdCancha(idCancha).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void desasociarEquipamientoDeCancha(Long idCancha, Long idEquipamiento) {
        if (!disponeRepository.existsById_IdCanchaAndId_IdEquipamiento(idCancha, idEquipamiento)) {
            throw new EntityNotFoundException(
                "Relación no encontrada para eliminar: Cancha ID " + idCancha + " y Equipamiento ID " + idEquipamiento
            );
        }
        disponeRepository.deleteById_IdCanchaAndId_IdEquipamiento(idCancha, idEquipamiento);
    }


    private DisponeDTO convertToDTO(Dispone entity) {
        return DisponeDTO.builder()
                .idCancha(entity.getCancha().getIdCancha())
                .idEquipamiento(entity.getEquipamiento().getIdEquipamiento())
                .cantidad(entity.getCantidad())
                .build();
    }

    // Lista cruda (siempre que uses EntityGraph aquí, está bien)
    @Override
    @Transactional(readOnly = true)
    public List<DisponeDTO> listarPorIdCancha(Long idCancha) {
        return disponeRepository.findById_IdCancha(idCancha)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisponeDTO> listarPorIdEquipamiento(Long idEquipamiento) {
        return disponeRepository.findByEquipamiento_IdEquipamiento(idEquipamiento)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    

}

package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.MacrodistritoDTO;
import com.espaciosdeportivos.dto.ZonaDTO;


import com.espaciosdeportivos.model.Zona;
import com.espaciosdeportivos.model.Macrodistrito;


import com.espaciosdeportivos.repository.MacrodistritoRepository;
import com.espaciosdeportivos.repository.ZonaRepository;

import com.espaciosdeportivos.service.IZonaService;
import com.espaciosdeportivos.validation.ZonaValidator;

//import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ZonaServiceImpl implements IZonaService {
    
    private final ZonaRepository zonaRepository;
    private final ZonaValidator zonaValidator;
    private final MacrodistritoRepository macrodistritoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ZonaDTO> obtenerTodasLasZonas() {
        return zonaRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                //.toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZonaDTO> ListarTodos() {
        return zonaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                //.collect(Collectors.toList());
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ZonaDTO obtenerZonaPorId(Long idZona) {
        Zona zona = zonaRepository.findById(idZona)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con ID: " + idZona));
        return convertToDTO(zona);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZonaDTO> buscarPorNombre(String nombre) {
        return zonaRepository.buscarPorNombre(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                //.toList();
    }

    @Override
    @Transactional
    public ZonaDTO crearZona(ZonaDTO zonaDTO) {
        zonaValidator.validarZona(zonaDTO);

        boolean existeMacrodistrito = macrodistritoRepository.existsById(zonaDTO.getIdMacrodistrito());
        if (!existeMacrodistrito) {
            throw new EntityNotFoundException("El macrodistrito con ID " + zonaDTO.getIdMacrodistrito() + " no existe.");

        }

        Zona zona = convertToEntity(zonaDTO);
        zona.setIdZona(null);
        zona.setEstado(Boolean.TRUE);

        Zona guardada = zonaRepository.save(zona);

        return convertToDTO(guardada);
    }

    
    @Override
    @Transactional
    public ZonaDTO actualizarZona(Long idZona, ZonaDTO zonaDTO) {
        Zona existente = zonaRepository.findById(idZona)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con ID: " + idZona));

        zonaValidator.validarZona(zonaDTO);

        boolean existeMacro = macrodistritoRepository.existsById(zonaDTO.getIdMacrodistrito());
        if (!existeMacro) {
            throw new EntityNotFoundException("Macrodistrito no encontrado con ID: " + zonaDTO.getIdMacrodistrito());
        }

        Macrodistrito macro = macrodistritoRepository.findById(zonaDTO.getIdMacrodistrito())
                .orElseThrow(() -> new EntityNotFoundException("Macrodistrito no encontrado con ID: " + zonaDTO.getIdMacrodistrito()));

        existente.setNombre(zonaDTO.getNombre());
        existente.setDescripcion(zonaDTO.getDescripcion());
        existente.setEstado(zonaDTO.getEstado());
        existente.setMacrodistrito(macro);

        Zona actualizada = zonaRepository.save(existente);

        return convertToDTO(actualizada);
    }


    @Override
    @Transactional
    public ZonaDTO eliminarZona(Long id , Boolean nuevoEstado) {
        Zona existente = zonaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con ID: " + id));
        existente.setEstado(nuevoEstado);
        return convertToDTO(zonaRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminarZonaFisicamente(Long id) {
        Zona existente = zonaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona no encontrado con ID: " + id)); 
        zonaRepository.delete(existente);
    }

    @Override
    @Transactional
    public Zona obtenerZonaConBloqueo(Long id) {
        Zona zona = zonaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona no encontrado con ID: " + id));
        try {
            Thread.sleep(15000); // Simula retardo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Vuelve a marcar el hilo como interrumpido
        }
        return zona;
    }

    

    // ---------- mapping ----------
    private ZonaDTO convertToDTO(Zona zona) {
        return ZonaDTO.builder()
                .idZona(zona.getIdZona())
                .nombre(zona.getNombre())
                .descripcion(zona.getDescripcion())
                .estado(zona.getEstado())
                .idMacrodistrito(zona.getMacrodistrito() != null ? zona.getMacrodistrito().getIdMacrodistrito() : null)
                .macrodistrito(convertMacrodistritoToDTO(zona.getMacrodistrito()))
                .build();
    }

    private Zona convertToEntity(ZonaDTO dto) {
        Macrodistrito macrodistrito = macrodistritoRepository.findById(dto.getIdMacrodistrito())
            .orElseThrow(() -> new RuntimeException("Macrodistrito no encontrado con ID: " + dto.getIdMacrodistrito()));

        return Zona.builder()
            .idZona(dto.getIdZona())
            .nombre(dto.getNombre())
            .descripcion(dto.getDescripcion())
            .estado(dto.getEstado() == null ? Boolean.TRUE : dto.getEstado())
            .macrodistrito(macrodistrito)
            .build();
    }
    private MacrodistritoDTO convertMacrodistritoToDTO(Macrodistrito z) {
        if (z == null) return null;
        return MacrodistritoDTO.builder()
                .idMacrodistrito(z.getIdMacrodistrito())
                .nombre(z.getNombre())
                .descripcion(z.getDescripcion())
                .estado(z.getEstado())
                .build();
    }

}
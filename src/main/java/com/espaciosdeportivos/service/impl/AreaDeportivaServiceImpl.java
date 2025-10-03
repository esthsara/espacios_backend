package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.AreaDeportivaDTO;
import com.espaciosdeportivos.model.AreaDeportiva;
import com.espaciosdeportivos.model.Zona;
import com.espaciosdeportivos.model.Administrador;

import com.espaciosdeportivos.repository.AreaDeportivaRepository;
import com.espaciosdeportivos.repository.ZonaRepository;
import com.espaciosdeportivos.repository.AdministradorRepository;

import com.espaciosdeportivos.service.IAreaDeportivaService;
import com.espaciosdeportivos.validation.AreaDeportivaValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaDeportivaServiceImpl implements IAreaDeportivaService {

    private final AreaDeportivaRepository areaDeportivaRepository;
    private final ZonaRepository zonaRepository;

    private final AdministradorRepository administradorRepository;
    private final AreaDeportivaValidator areaDeportivaValidator;

    @Autowired
    public AreaDeportivaServiceImpl(
        AreaDeportivaRepository areaDeportivaRepository, 
        ZonaRepository zonaRepository, 
        AdministradorRepository administradorRepository, 
        AreaDeportivaValidator areaDeportivaValidator
    ) {
        this.areaDeportivaRepository = areaDeportivaRepository;
        this.zonaRepository = zonaRepository;
        this.administradorRepository = administradorRepository;
        this.areaDeportivaValidator = areaDeportivaValidator;
    }


    @Override
    @Transactional(readOnly = true)
    public List<AreaDeportivaDTO> listarTodos() {
        return areaDeportivaRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<AreaDeportivaDTO> obtenerTodasLasAreasDeportivas() {
        return areaDeportivaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AreaDeportivaDTO obtenerAreaDeportivaPorId(Long id) {
        AreaDeportiva area = areaDeportivaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área deportiva no encontrada con ID: " + id));
        return convertToDTO(area);
    }

    @Override
    public AreaDeportivaDTO crearAreaDeportiva(AreaDeportivaDTO areaDTO) {
        areaDeportivaValidator.validarArea(areaDTO);

        boolean existeAdministrador = administradorRepository.existsById(areaDTO.getId());
        if (!existeAdministrador) {
            throw new EntityNotFoundException("El Administrador con ID " + areaDTO.getId() + " no existe.");
        }

        AreaDeportiva area = convertToEntity(areaDTO);
        area.setIdAreaDeportiva(null);
        area.setEstado(Boolean.TRUE);

        AreaDeportiva guardada = areaDeportivaRepository.save(area);

        return convertToDTO(guardada);

    }

    @Override
    public AreaDeportivaDTO actualizarAreaDeportiva(Long id, @Valid AreaDeportivaDTO dto) {
        AreaDeportiva existente = areaDeportivaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área deportiva no encontrada con ID: " + id));
        areaDeportivaValidator.validarArea(dto);

        Zona zona = zonaRepository.findById(dto.getIdZona())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con ID: " + dto.getIdZona()));
        Administrador admin = administradorRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con ID: " + dto.getId()));

        existente.setNombreArea(dto.getNombreArea());
        existente.setDescripcionArea(dto.getDescripcionArea());
        existente.setEmailArea(dto.getEmailArea());
        existente.setTelefonoArea(dto.getTelefonoArea());
        existente.setHoraInicioArea(dto.getHoraInicioArea() != null ? dto.getHoraInicioArea().toString() : null);
        existente.setHoraFinArea(dto.getHoraFinArea() != null ? dto.getHoraFinArea().toString() : null);
        //existente.setEstadoArea(dto.getEstadoArea());
        existente.setUrlImagen(dto.getUrlImagen());
        existente.setLatitud(dto.getLatitud());
        existente.setLongitud(dto.getLongitud());
        existente.setEstado(dto.getEstado());
        existente.setZona(zona);
        existente.setAdministrador(admin);

        AreaDeportiva actualizada = areaDeportivaRepository.save(existente);
        return convertToDTO(actualizada);
    }

    @Override
    @Transactional
    public void eliminarAreaDeportivaFisicamente(Long id) {
        AreaDeportiva existente = areaDeportivaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Área deportiva no encontrada con ID: " + id));

        areaDeportivaRepository.delete(existente);
    }

    @Override
    @Transactional
    public AreaDeportivaDTO eliminarAreaDeportiva(Long idarea , Boolean nuevoEstado) {
        AreaDeportiva existente = areaDeportivaRepository.findByIdAreaDeportivaAndEstadoTrue(idarea)
                .orElseThrow(() -> new RuntimeException("Área deportiva no encontrada con ID: " + idarea));
        existente.setEstado(nuevoEstado);
        return convertToDTO(areaDeportivaRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaDeportivaDTO> buscarPorNombre(String nombre) {
        return areaDeportivaRepository.buscarPorNombre(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public AreaDeportiva obtenerAreaDeportivaConBloqueo(Long id) {
        AreaDeportiva areaDeportiva = areaDeportivaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area deportiba  no encontrado con ID: " + id));
        try {
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return areaDeportiva;
    }

    


    // ---------- mapping ----------
    private AreaDeportivaDTO convertToDTO(AreaDeportiva a) {
        return AreaDeportivaDTO.builder()
                .idAreadeportiva(a.getIdAreaDeportiva())
                .nombreArea(a.getNombreArea())
                .descripcionArea(a.getDescripcionArea())
                .emailArea(a.getEmailArea())
                .telefonoArea(a.getTelefonoArea())
                .horaInicioArea(parseTime(a.getHoraInicioArea()))
                .horaFinArea(parseTime(a.getHoraFinArea()))
                //.estadoArea(a.getEstadoArea())
                .urlImagen(a.getUrlImagen())
                .latitud(a.getLatitud())
                .longitud(a.getLongitud())
                .estado(a.getEstado())
                .idZona(a.getZona() != null ? a.getZona().getIdZona() : null)
                .id(a.getAdministrador() != null ? a.getAdministrador().getId() : null)
                .build();
    }

    private AreaDeportiva convertToEntity(AreaDeportivaDTO d) {
        Administrador administrador = administradorRepository.findById(d.getId())
                .orElseThrow(() -> new RuntimeException("Admin no encontrada con ID: " + d.getId()));        
        Zona zona = zonaRepository.findById(d.getIdZona())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con ID: " + d.getIdZona()));

        return AreaDeportiva.builder()
                .idAreaDeportiva(d.getIdAreadeportiva())
                .nombreArea(d.getNombreArea())
                .descripcionArea(d.getDescripcionArea())
                .emailArea(d.getEmailArea())
                .telefonoArea(d.getTelefonoArea())
                .horaInicioArea(d.getHoraInicioArea() != null ? d.getHoraInicioArea().toString() : null)
                .horaFinArea(d.getHoraFinArea() != null ? d.getHoraFinArea().toString() : null)
                //.estadoArea(d.getEstadoArea())
                .urlImagen(d.getUrlImagen())
                .latitud(d.getLatitud())
                .longitud(d.getLongitud())
                .estado(d.getEstado() != null ? d.getEstado() : Boolean.TRUE)
                .zona(zona)
                .administrador(administrador)
                .build();
    }

    private LocalTime parseTime(String t) {
        return (t != null && !t.isBlank()) ? LocalTime.parse(t) : null;
    }
}

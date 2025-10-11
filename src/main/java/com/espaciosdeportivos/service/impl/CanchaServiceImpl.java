package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.AreaDeportivaDTO; // objeto front K
import com.espaciosdeportivos.dto.ZonaDTO; // objeto front K

import com.espaciosdeportivos.model.Cancha;
import com.espaciosdeportivos.model.AreaDeportiva;
import com.espaciosdeportivos.model.Zona;

import com.espaciosdeportivos.repository.CanchaRepository;
import com.espaciosdeportivos.repository.AreaDeportivaRepository;

import com.espaciosdeportivos.service.ICanchaService;
import com.espaciosdeportivos.validation.CanchaValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanchaServiceImpl implements ICanchaService {

    private final CanchaRepository canchaRepository;
    private final AreaDeportivaRepository areaDeportivaRepository;
    private final CanchaValidator canchaValidator;

    @Autowired
    public CanchaServiceImpl(
        CanchaRepository canchaRepository, 
        AreaDeportivaRepository areaDeportivaRepository, 
        CanchaValidator canchaValidator
    ) {
        this.canchaRepository = canchaRepository;
        this.areaDeportivaRepository = areaDeportivaRepository;
        this.canchaValidator = canchaValidator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CanchaDTO> obtenerTodasLasCanchas() {
        return canchaRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CanchaDTO> ListarTodos() {
        return canchaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CanchaDTO obtenerCanchaPorId(Long id) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));
        return convertToDTO(cancha);
    }

    @Override
    public CanchaDTO crearCancha(CanchaDTO dto) {
        canchaValidator.validarCancha(dto);

        boolean existeAreaDportiva = areaDeportivaRepository.existsById(dto.getIdAreadeportiva());
        if (!existeAreaDportiva) {
            throw new RuntimeException("Área deportiva no encontrada con ID: " + dto.getIdAreadeportiva());
        }

        Cancha cancha = convertToEntity(dto);
        cancha.setIdCancha(null);
        cancha.setEstado(Boolean.TRUE);
        Cancha guardada = canchaRepository.save(cancha);

        return convertToDTO(guardada);
    }

    @Override
    public CanchaDTO actualizarCancha(Long id, @Valid CanchaDTO dto) {
        Cancha existente = canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));

        canchaValidator.validarCancha(dto);

        AreaDeportiva area = areaDeportivaRepository.findById(dto.getIdAreadeportiva())
                .orElseThrow(() -> new RuntimeException("Área deportiva no encontrada con ID: " + dto.getIdAreadeportiva()));

        existente.setNombre(dto.getNombre());
        existente.setCostoHora(dto.getCostoHora());
        existente.setCapacidad(dto.getCapacidad());
        existente.setEstado(dto.getEstado());
        existente.setMantenimiento(dto.getMantenimiento());
        existente.setHoraInicio(dto.getHoraInicio());
        existente.setHoraFin(dto.getHoraFin());
        existente.setTipoSuperficie(dto.getTipoSuperficie());
        existente.setTamano(dto.getTamano());
        existente.setIluminacion(dto.getIluminacion());
        existente.setCubierta(dto.getCubierta());
        existente.setUrlImagen(dto.getUrlImagen());
        existente.setAreaDeportiva(area);

        Cancha actualizada = canchaRepository.save(existente);
        return convertToDTO(actualizada);
    }

    @Override
    @Transactional
    public void eliminarCanchaFisicamente(Long id) {
        Cancha existente = canchaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));

        canchaRepository.delete(existente);
    }

    @Override
    public CanchaDTO eliminarCancha(Long id, Boolean nuevoEstado) {
        Cancha existente = canchaRepository.findByIdCanchaAndEstadoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));
        existente.setEstado(nuevoEstado);
        return convertToDTO(canchaRepository.save(existente));
    }

    @Override
    @Transactional
    public Cancha obtenerCanchaConBloqueo(Long id) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));
        try {
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return cancha;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CanchaDTO> buscarPorNombre(String nombre) {
        return canchaRepository.buscarPorNombre(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CanchaDTO> BuscarConFiltros(LocalTime horaInicio, LocalTime horaFin, Double costo, Integer capacidad,
                                            String tamano, String iluminacion, String cubierta) {
        return canchaRepository.buscarFiltros(horaInicio, horaFin, costo, capacidad, tamano, iluminacion, cubierta)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // --------- mapping ----------
    private CanchaDTO convertToDTO(Cancha c) {
        AreaDeportiva area = c.getAreaDeportiva(); // objeto front K

        return CanchaDTO.builder()
                .idCancha(c.getIdCancha())
                .nombre(c.getNombre())
                .costoHora(c.getCostoHora())
                .capacidad(c.getCapacidad())
                .estado(c.getEstado())
                .mantenimiento(c.getMantenimiento())
                .horaInicio(c.getHoraInicio())
                .horaFin(c.getHoraFin())
                .tipoSuperficie(c.getTipoSuperficie())
                .tamano(c.getTamano())
                .iluminacion(c.getIluminacion())
                .cubierta(c.getCubierta())
                .urlImagen(c.getUrlImagen())
                .idAreadeportiva(area != null ? area.getIdAreaDeportiva() : null)
                .areaDeportiva(area != null ? convertAreaToDTO(area) : null) // objeto front K
                .build();
    }

    private Cancha convertToEntity(CanchaDTO d) {
        AreaDeportiva area = areaDeportivaRepository.findById(d.getIdAreadeportiva())
                .orElseThrow(() -> new RuntimeException("Área deportiva no encontrada con ID: " + d.getIdAreadeportiva()));
        return Cancha.builder()
                .idCancha(d.getIdCancha())
                .nombre(d.getNombre())
                .costoHora(d.getCostoHora())
                .capacidad(d.getCapacidad())
                .estado(d.getEstado() != null ? d.getEstado() : Boolean.TRUE)
                .mantenimiento(d.getMantenimiento())
                .horaInicio(d.getHoraInicio())
                .horaFin(d.getHoraFin())
                .tipoSuperficie(d.getTipoSuperficie())
                .tamano(d.getTamano())
                .iluminacion(d.getIluminacion())
                .cubierta(d.getCubierta())
                .urlImagen(d.getUrlImagen())
                .areaDeportiva(area)
                .build();
    }

    // objeto front K
    private AreaDeportivaDTO convertAreaToDTO(AreaDeportiva a) {
        return AreaDeportivaDTO.builder()
                .idAreadeportiva(a.getIdAreaDeportiva())
                .nombreArea(a.getNombreArea())
                .descripcionArea(a.getDescripcionArea())
                .emailArea(a.getEmailArea())
                .telefonoArea(a.getTelefonoArea())
                .horaInicioArea(parseTime(a.getHoraInicioArea()))
                .horaFinArea(parseTime(a.getHoraFinArea()))
                .urlImagen(a.getUrlImagen())
                .latitud(a.getLatitud())
                .longitud(a.getLongitud())
                .estado(a.getEstado())
                .idZona(a.getZona() != null ? a.getZona().getIdZona() : null)
                .zona(a.getZona() != null ? convertZonaToDTO(a.getZona()) : null) // objeto front K
                .id(a.getAdministrador() != null ? a.getAdministrador().getId() : null)
                .build();
    }

    // objeto front K
    private ZonaDTO convertZonaToDTO(Zona z) {
        if (z == null) return null;
        return ZonaDTO.builder()
                .idZona(z.getIdZona()) // objeto front K
                .nombre(z.getNombre()) // objeto front K
                .descripcion(z.getDescripcion()) // objeto front K
                .estado(z.getEstado()) // objeto front K
                .idMacrodistrito(z.getMacrodistrito() != null ? z.getMacrodistrito().getIdMacrodistrito() : null) // objeto front K
                .build();
    }

    private LocalTime parseTime(String t) {
        return (t != null && !t.isBlank()) ? LocalTime.parse(t) : null;
    }
}
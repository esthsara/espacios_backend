package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.AreaDeportivaDTO;
import com.espaciosdeportivos.dto.MacrodistritoDTO;
//import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.ZonaDTO; // objeto front K
import com.espaciosdeportivos.model.AreaDeportiva;
import com.espaciosdeportivos.model.Macrodistrito;
//import com.espaciosdeportivos.model.Cancha;
import com.espaciosdeportivos.model.Zona;
import com.espaciosdeportivos.model.Administrador;

import com.espaciosdeportivos.repository.AreaDeportivaRepository;
import com.espaciosdeportivos.repository.ZonaRepository;
import com.espaciosdeportivos.repository.AdministradorRepository;

import com.espaciosdeportivos.service.IAreaDeportivaService;
import com.espaciosdeportivos.service.ImagenService;
import com.espaciosdeportivos.validation.AreaDeportivaValidator;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AreaDeportivaServiceImpl implements IAreaDeportivaService {

    private final AreaDeportivaRepository areaDeportivaRepository;
    private final ZonaRepository zonaRepository;
    private final AdministradorRepository administradorRepository;
    private final AreaDeportivaValidator areaDeportivaValidator;

    private final ImagenService imagenService;
    private static final String ENTIDAD_TIPO = "AREADEPORTIVA";


    /*@Autowired
    public AreaDeportivaServiceImpl(
        AreaDeportivaRepository areaDeportivaRepository, 
        ZonaRepository zonaRepository, 
        AdministradorRepository administradorRepository, 
        AreaDeportivaValidator areaDeportivaValidator,
        ImagenService imagenService
    ) {
        this.areaDeportivaRepository = areaDeportivaRepository;
        this.zonaRepository = zonaRepository;
        this.administradorRepository = administradorRepository;
        this.areaDeportivaValidator = areaDeportivaValidator;
        this.imagenService = imagenService;
    }*/

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
        existente.setHoraInicioArea(dto.getHoraInicioArea() != null ? dto.getHoraInicioArea() : null);
        existente.setHoraFinArea(dto.getHoraFinArea() != null ? dto.getHoraFinArea(): null);
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
                .orElseThrow(() -> new RuntimeException("Área deportiva no encontrada con ID: " + id));
        try {
            Thread.sleep(15000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return areaDeportiva;
    }

         // objeto front K
    /*private ZonaDTO convertZonaToDTO(Zona z) {
        if (z == null) return null;
        return ZonaDTO.builder()
                .idZona(z.getIdZona()) // objeto front K
                .nombre(z.getNombre()) // objeto front K
                .descripcion(z.getDescripcion()) // objeto front K
                .estado(z.getEstado()) // objeto front K
                .idMacrodistrito(z.getMacrodistrito() != null ? z.getMacrodistrito().getIdMacrodistrito() : null) // objeto front K
                .build();
    }*/

     // ==========================================================
// 🖼️ MÉTODOS DE GESTIÓN DE IMÁGENES PARA CANCHAS
// ==========================================================

    @Override
    @Transactional
    public AreaDeportivaDTO agregarImagenes(Long idAreadeportiva, List<MultipartFile> archivosImagenes) {
        log.info("📸 Agregando {} imágenes a la area ID: {}", archivosImagenes.size(), idAreadeportiva);

        AreaDeportiva area = areaDeportivaRepository.findByIdAreaDeportivaAndEstadoTrue(idAreadeportiva)
                .orElseThrow(() -> new RuntimeException("area no encontrada o inactiva"));

        imagenService.guardarImagenesParaEntidad(archivosImagenes, ENTIDAD_TIPO, idAreadeportiva);
        log.info("Imágenes agregadas exitosamente a la area {}", idAreadeportiva);

        return obtenerAreaDeportivaPorId(idAreadeportiva);
    }

    @Override
    @Transactional
    public AreaDeportivaDTO eliminarImagen(Long idAreadeportiva, Long idImagenRelacion) {
        log.info("Eliminando imagen {} de la area {}", idImagenRelacion, idAreadeportiva);

        areaDeportivaRepository.findByIdAreaDeportivaAndEstadoTrue(idAreadeportiva)
                .orElseThrow(() -> new RuntimeException("area no encontrada o inactiva"));

        imagenService.eliminarImagenLogicamente(idImagenRelacion);
        log.info("Imagen eliminada correctamente");

        return obtenerAreaDeportivaPorId(idAreadeportiva);
    }

    @Override
    @Transactional
    public AreaDeportivaDTO reordenarImagenes(Long idAreadeportiva, List<Long> idsImagenesOrden) {
        log.info("Reordenando {} imágenes de la area {}", idsImagenesOrden.size(), idAreadeportiva);

        areaDeportivaRepository.findByIdAreaDeportivaAndEstadoTrue(idAreadeportiva)
                .orElseThrow(() -> new RuntimeException("area no encontrada o inactiva"));

        imagenService.reordenarImagenes(ENTIDAD_TIPO, idAreadeportiva, idsImagenesOrden);
        log.info("Imágenes reordenadas con éxito");

        return obtenerAreaDeportivaPorId(idAreadeportiva);
    }


    // ---------- mapping ----------
    private AreaDeportivaDTO convertToDTO(AreaDeportiva a) {
        return AreaDeportivaDTO.builder()
                .idAreadeportiva(a.getIdAreaDeportiva())
                .nombreArea(a.getNombreArea())
                .descripcionArea(a.getDescripcionArea())
                .emailArea(a.getEmailArea())
                .telefonoArea(a.getTelefonoArea())
                .horaInicioArea(a.getHoraInicioArea())
                .horaFinArea(a.getHoraFinArea())
                .urlImagen(a.getUrlImagen())
                .latitud(a.getLatitud())
                .longitud(a.getLongitud())
                .estado(a.getEstado())
                .idZona(a.getZona() != null ? a.getZona().getIdZona() : null)
                .zona(convertZonaToDTO(a.getZona())) // objeto front K
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
                .horaInicioArea(d.getHoraInicioArea() != null ? d.getHoraInicioArea(): null)
                .horaFinArea(d.getHoraFinArea() != null ? d.getHoraFinArea() : null)
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

     // objeto front 
    private ZonaDTO convertZonaToDTO(Zona z) {
        if (z == null) return null;
        return ZonaDTO.builder()
                .idZona(z.getIdZona()) // objeto front K
                .nombre(z.getNombre()) // objeto front K
                .descripcion(z.getDescripcion()) // objeto front K
                .estado(z.getEstado()) // objeto front K
                .idMacrodistrito(z.getMacrodistrito() != null ? z.getMacrodistrito().getIdMacrodistrito() : null) // objeto front K
                .macrodistrito(convertMacrodistritoToDTO(z.getMacrodistrito()))
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

    @Override
    public AreaDeportivaDTO obtenerPorAdminId(Long Id) {
        AreaDeportiva area = areaDeportivaRepository.findByAdministrador_Id(Id)
            .orElseThrow(() -> new RuntimeException("Área no encontrada para el administrador"));
        return convertToDTO(area);
    }

    //MI_AREA k actualizar por adminId   
    //Comentario : "rocio cambien las fechas ya no sons tring espero que no te afecte mucho perdon"
    @Override
    public AreaDeportivaDTO actualizarPorAdminId(Long adminId, AreaDeportivaDTO dto) {
        AreaDeportiva area = areaDeportivaRepository.findByAdministrador_Id(adminId)
            .orElseThrow(() -> new RuntimeException("Área no encontrada para el administrador"));

        areaDeportivaValidator.validarArea(dto); // validación como en otros métodos

        Zona zona = zonaRepository.findById(dto.getIdZona())
            .orElseThrow(() -> new RuntimeException("Zona no encontrada con ID: " + dto.getIdZona()));

        // Actualiza los campos permitidos
        area.setNombreArea(dto.getNombreArea());
        area.setDescripcionArea(dto.getDescripcionArea());
        area.setEmailArea(dto.getEmailArea());
        area.setTelefonoArea(dto.getTelefonoArea());
        area.setHoraInicioArea(dto.getHoraInicioArea() != null ? dto.getHoraInicioArea(): null);
        area.setHoraFinArea(dto.getHoraFinArea() != null ? dto.getHoraFinArea() : null);
        area.setUrlImagen(dto.getUrlImagen());
        area.setLatitud(dto.getLatitud());
        area.setLongitud(dto.getLongitud());
        area.setEstado(dto.getEstado());
        area.setZona(zona);

        AreaDeportiva actualizada = areaDeportivaRepository.save(area);
        return convertToDTO(actualizada);
    }





}

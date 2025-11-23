package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.dto.AreaDeportivaDTO;
import com.espaciosdeportivos.dto.ZonaDTO;
import com.espaciosdeportivos.dto.ImagenDTO;

import com.espaciosdeportivos.model.Cancha;
import com.espaciosdeportivos.model.Disciplina;
import com.espaciosdeportivos.model.Equipamiento;
import com.espaciosdeportivos.model.sepractica;
import com.espaciosdeportivos.model.AreaDeportiva;
import com.espaciosdeportivos.model.Zona;
import com.espaciosdeportivos.model.dispone;

import com.espaciosdeportivos.repository.CanchaRepository;
import com.espaciosdeportivos.repository.AreaDeportivaRepository;
import com.espaciosdeportivos.repository.EquipamientoRepository;
import com.espaciosdeportivos.repository.disponeRepository;
import com.espaciosdeportivos.repository.incluyeRepository;
import com.espaciosdeportivos.repository.sepracticaRepository;

import com.espaciosdeportivos.service.ICanchaService;
import com.espaciosdeportivos.service.ImagenService;
import com.espaciosdeportivos.validation.CanchaValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CanchaServiceImpl implements ICanchaService {

    private final CanchaRepository canchaRepository;
    private final AreaDeportivaRepository areaDeportivaRepository;
    private final CanchaValidator canchaValidator;
    private final EquipamientoRepository equipamientoRepository;
    private final disponeRepository disponeRepository;
    private final incluyeRepository incluyeRepository;
    private final sepracticaRepository sepracticaRepository;
    private final ImagenService imagenService;

    private static final String ENTIDAD_TIPO_CANCHA = "CANCHA";
    private static final String ENTIDAD_TIPO_AREA = "AREADEPORTIVA";

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
                .orElseThrow(
                        () -> new RuntimeException("Área deportiva no encontrada con ID: " + dto.getIdAreadeportiva()));

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
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public List<EquipamientoDTO> obtenerEquipamientoPorCancha(Long canchaId) {
        List<dispone> lista = disponeRepository.findByCanchaIdCancha(canchaId);
        return lista.stream()
                .map(d -> convertEquipamientoToDTO(d.getEquipamiento()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaDTO> obtenerDiciplinasPorCancha(Long canchaId) {
        List<sepractica> lista = sepracticaRepository.obtenerPorCancha(canchaId);
        return lista.stream()
                .map(d -> convertDiciplinaToDTO(d.getDisciplina()))
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 🖼️ MÉTODOS DE GESTIÓN DE IMÁGENES PARA CANCHAS
    // ==========================================================

    @Override
    @Transactional
    public CanchaDTO agregarImagenes(Long idCancha, List<MultipartFile> archivosImagenes) {
        log.info("📸 Agregando {} imágenes a la cancha ID: {}", archivosImagenes.size(), idCancha);

        Cancha cancha = canchaRepository.findByIdCanchaAndEstadoTrue(idCancha)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada o inactiva"));

        imagenService.guardarImagenesParaEntidad(archivosImagenes, ENTIDAD_TIPO_CANCHA, idCancha);
        log.info("Imágenes agregadas exitosamente a la cancha {}", idCancha);

        return obtenerCanchaPorId(idCancha);
    }

    @Override
    @Transactional
    public CanchaDTO eliminarImagen(Long idCancha, Long idImagenRelacion) {
        log.info("🗑️ Eliminando imagen {} de la cancha {}", idImagenRelacion, idCancha);

        canchaRepository.findByIdCanchaAndEstadoTrue(idCancha)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada o inactiva"));

        imagenService.eliminarImagenLogicamente(idImagenRelacion);
        log.info("Imagen eliminada correctamente");

        return obtenerCanchaPorId(idCancha);
    }

    @Override
    @Transactional
    public CanchaDTO reordenarImagenes(Long idCancha, List<Long> idsImagenesOrden) {
        log.info(" Reordenando {} imágenes de la cancha {}", idsImagenesOrden.size(), idCancha);

        canchaRepository.findByIdCanchaAndEstadoTrue(idCancha)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada o inactiva"));

        imagenService.reordenarImagenes(ENTIDAD_TIPO_CANCHA, idCancha, idsImagenesOrden);
        log.info("Imágenes reordenadas con éxito");

        return obtenerCanchaPorId(idCancha);
    }

    @Override
    public List<CanchaDTO> obtenerCanchasPorArea(Long idArea) {
        List<Cancha> canchas = canchaRepository.findByAreaDeportiva_IdAreaDeportiva(idArea);
        return canchas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // --------- MAPEO ----------
    private CanchaDTO convertToDTO(Cancha c) {
        if (c == null)
            return null;
        AreaDeportiva area = c.getAreaDeportiva();

        CanchaDTO dto = CanchaDTO.builder()
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
                .idAreadeportiva(c.getAreaDeportiva() != null ? c.getAreaDeportiva().getIdAreaDeportiva() : null)
                .areaDeportiva(area != null ? convertAreaToDTO(area) : null)
                .build();

        // Cargar disciplinas
        try {
            List<sepractica> sePracticas = sepracticaRepository.findByCanchaIdCancha(c.getIdCancha());
            List<DisciplinaDTO> disciplinas = sePracticas.stream()
                    .map(sp -> convertDiciplinaToDTO(sp.getDisciplina()))
                    .toList();
            dto.setDisciplinas(disciplinas);
        } catch (Exception e) {
            dto.setDisciplinas(List.of());
        }

        // Cargar equipamientos
        try {
            List<dispone> disponeList = disponeRepository.findByCanchaIdCancha(c.getIdCancha());
            List<EquipamientoDTO> equipamientos = disponeList.stream()
                    .map(d -> convertEquipamientoToDTO(d.getEquipamiento()))
                    .toList();
            dto.setEquipamientos(equipamientos);
        } catch (Exception e) {
            dto.setEquipamientos(List.of());
        }

        // Cargar imágenes
        try {
            List<ImagenDTO> imagenes = imagenService.obtenerImagenesPorEntidad(ENTIDAD_TIPO_CANCHA, c.getIdCancha());
            dto.setImagenes(imagenes);
        } catch (Exception e) {
            dto.setImagenes(List.of());
        }

        return dto;
    }

    private DisciplinaDTO convertDiciplinaToDTO(Disciplina d) {
        return DisciplinaDTO.builder()
                .idDisciplina(d.getIdDisciplina())
                .nombre(d.getNombre())
                .descripcion(d.getDescripcion())
                .estado(d.getEstado())
                .build();
    }

    private EquipamientoDTO convertEquipamientoToDTO(Equipamiento e) {
        return EquipamientoDTO.builder()
                .idEquipamiento(e.getIdEquipamiento())
                .nombreEquipamiento(e.getNombreEquipamiento())
                .estado(e.getEstado())
                .descripcion(e.getDescripcion())
                .tipoEquipamiento(e.getTipoEquipamiento())
                .build();
    }

    private Cancha convertToEntity(CanchaDTO d) {
        AreaDeportiva area = areaDeportivaRepository.findById(d.getIdAreadeportiva())
                .orElseThrow(
                        () -> new RuntimeException("Área deportiva no encontrada con ID: " + d.getIdAreadeportiva()));
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

    // --------------------------------------------------------------------
    // CORRECCIÓN AQUÍ: Mapeo de Área SIN usar urlImagen
    // --------------------------------------------------------------------
    private AreaDeportivaDTO convertAreaToDTO(AreaDeportiva a) {
        // Buscamos las imágenes del área para llenar el DTO correctamente
        List<ImagenDTO> imagenesArea = List.of();
        try {
            imagenesArea = imagenService.obtenerImagenesPorEntidad(ENTIDAD_TIPO_AREA, a.getIdAreaDeportiva());
        } catch (Exception e) {
            log.warn("No se pudieron cargar las imágenes para el área embebida en cancha", e);
        }

        return AreaDeportivaDTO.builder()
                .idAreadeportiva(a.getIdAreaDeportiva())
                .nombreArea(a.getNombreArea())
                .descripcionArea(a.getDescripcionArea())
                .emailArea(a.getEmailArea())
                .telefonoArea(a.getTelefonoArea())
                .horaInicioArea(a.getHoraInicioArea())
                .horaFinArea(a.getHoraFinArea())
                // .urlImagen(a.getUrlImagen()) // ELIMINADO CORRECTAMENTE
                .imagenes(imagenesArea) // Asignamos la lista de imágenes
                .latitud(a.getLatitud())
                .longitud(a.getLongitud())
                .estado(a.getEstado())
                .idZona(a.getZona() != null ? a.getZona().getIdZona() : null)
                .zona(a.getZona() != null ? convertZonaToDTO(a.getZona()) : null)
                .id(a.getAdministrador() != null ? a.getAdministrador().getId() : null)
                .build();
    }

    private ZonaDTO convertZonaToDTO(Zona z) {
        if (z == null)
            return null;
        return ZonaDTO.builder()
                .idZona(z.getIdZona())
                .nombre(z.getNombre())
                .descripcion(z.getDescripcion())
                .estado(z.getEstado())
                .idMacrodistrito(z.getMacrodistrito() != null ? z.getMacrodistrito().getIdMacrodistrito() : null)
                .build();
    }
}
package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.CanchaDTO;
import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.EquipamientoDTO;
import com.espaciosdeportivos.dto.ReservaDTO;
import com.espaciosdeportivos.dto.AreaDeportivaDTO; // objeto front K
import com.espaciosdeportivos.dto.ZonaDTO; // objeto front K
import com.espaciosdeportivos.dto.DisponeDTO;

import com.espaciosdeportivos.model.Cancha;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.model.Disciplina;
import com.espaciosdeportivos.model.Equipamiento;
import com.espaciosdeportivos.model.Reserva;
import com.espaciosdeportivos.model.Sepractica;
import com.espaciosdeportivos.model.AreaDeportiva;
import com.espaciosdeportivos.model.Zona;
import com.espaciosdeportivos.model.Dispone;
import com.espaciosdeportivos.model.Incluye;
import com.espaciosdeportivos.model.Macrodistrito;
//import com.espaciosdeportivos.model.sepractica;
import com.espaciosdeportivos.repository.CanchaRepository;
import com.espaciosdeportivos.repository.AreaDeportivaRepository;
import com.espaciosdeportivos.repository.EquipamientoRepository;
import com.espaciosdeportivos.repository.disponeRepository;
import com.espaciosdeportivos.repository.IncluyeRepository;
import com.espaciosdeportivos.repository.sepracticaRepository;

import com.espaciosdeportivos.service.ICanchaService;
import com.espaciosdeportivos.validation.CanchaValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

//import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.dto.MacrodistritoDTO;
import com.espaciosdeportivos.service.ImagenService;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final IncluyeRepository incluyeRepository;
    private final sepracticaRepository sepracticaRepository;
    private final ImagenService imagenService;

    private static final String ENTIDAD_TIPO = "CANCHA";

    /*@Autowired
    public CanchaServiceImpl(
        CanchaRepository canchaRepository, 
        AreaDeportivaRepository areaDeportivaRepository, 
        CanchaValidator canchaValidator,
        disponeRepository disponeRepository,
        EquipamientoRepository equipamientoRepository,
        incluyeRepository incluyeRepository,
        //sepracticaRepository sepracticaRepository
        ImagenService imagenService
        
    ) {
        this.canchaRepository = canchaRepository;
        this.areaDeportivaRepository = areaDeportivaRepository;
        this.canchaValidator = canchaValidator;
        this.equipamientoRepository = equipamientoRepository;
        this.disponeRepository = disponeRepository;
        this.incluyeRepository = incluyeRepository;
        this.imagenService = imagenService;
        //this.sepracticaRepository = sepracticaRepository;
    }*/

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
        List<Dispone> lista = disponeRepository.findByCanchaIdCancha(canchaId);

        return lista.stream()
                .map(d -> convertEquipamientoToDTO(d.getEquipamiento()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaDTO> obtenerDiciplinasPorCancha(Long canchaId){
        List<Sepractica> lista = sepracticaRepository.obtenerPorCancha(canchaId);
        return lista.stream()
                    .map(d -> convertDiciplinaToDTO(d.getDisciplina()))
                    .collect(Collectors.toList());
    }
    
    /*@Override
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerReservaPorCancha(Long canchaId) {
        List<Incluye> lista = incluyeRepository.findByCanchaIdCancha(canchaId);

        return lista.stream()
                .map(i -> convertReservaToDTO(i.getReserva()))
                .collect(Collectors.toList());
    }*/

    // ==========================================================
// 🖼️ MÉTODOS DE GESTIÓN DE IMÁGENES PARA CANCHAS
// ==========================================================

    @Override
    @Transactional
    public CanchaDTO agregarImagenes(Long idCancha, List<MultipartFile> archivosImagenes) {
        log.info("📸 Agregando {} imágenes a la cancha ID: {}", archivosImagenes.size(), idCancha);

        Cancha cancha = canchaRepository.findByIdCanchaAndEstadoTrue(idCancha)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada o inactiva"));

        imagenService.guardarImagenesParaEntidad(archivosImagenes, ENTIDAD_TIPO, idCancha);
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

        imagenService.reordenarImagenes(ENTIDAD_TIPO, idCancha, idsImagenesOrden);
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
        if (c == null) return null;
        AreaDeportiva area = c.getAreaDeportiva(); // objeto front K

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
                .idAreadeportiva(c.getAreaDeportiva() != null ? c.getAreaDeportiva().getIdAreaDeportiva() : null    )
                .areaDeportiva(area != null ? convertAreaToDTO(area) : null)
                .build();

        // >>> Cargar disciplinas desde la tabla intermedia <<<
        try {
            List<Sepractica> sePracticas = sepracticaRepository.findByCanchaIdCancha(c.getIdCancha());
            List<DisciplinaDTO> disciplinas = sePracticas.stream()
                .map(sp -> convertDiciplinaToDTO(sp.getDisciplina()))
                .toList();
            dto.setDisciplinas(disciplinas);
        } catch (Exception e) {
            log.warn("Error cargando disciplinas para cancha {}", c.getIdCancha(), e);
            dto.setDisciplinas(List.of());
        }

        // >>> Cargar equipamientos desde la tabla intermedia <<<
        try {
            List<Dispone> disponeList = disponeRepository.findByCanchaIdCancha(c.getIdCancha());
            List<EquipamientoDTO> equipamientos = disponeList.stream()
                .map(d -> convertEquipamientoToDTO(d.getEquipamiento()))
                .toList();
            dto.setEquipamientos(equipamientos);
        } catch (Exception e) {
            log.warn("Error cargando equipamientos para cancha {}", c.getIdCancha(), e);
            dto.setEquipamientos(List.of());
        }  
        
        // >>> Cargar imágenes asociadas a la cancha <<<
        try {
            List<ImagenDTO> imagenes = imagenService.obtenerImagenesPorEntidad(ENTIDAD_TIPO, c.getIdCancha());
            dto.setImagenes(imagenes);
        } catch (Exception e) {
            log.warn("Error cargando imágenes para cancha {}: {}", c.getIdCancha(), e.getMessage());
            dto.setImagenes(List.of());
        }

        return dto;
    }

    //---diciplina--
    private DisciplinaDTO convertDiciplinaToDTO(Disciplina d) {
        return DisciplinaDTO.builder()
                .idDisciplina(d.getIdDisciplina())
                .nombre(d.getNombre())
                .descripcion(d.getDescripcion())
                .estado(d.getEstado())
                .build();
    }
    //---reserva------- Comentario : esta no lo hize por que no va creo que aqui va mejor en reserva
    /*private ReservaDTO convertReservaToDTO(Reserva reserva) {
        Cliente cliente = reserva.getCliente();

        return ReservaDTO.builder()
                .idReserva(reserva.getIdReserva())
                .fechaReserva(reserva.getFechaReserva())
                .horaInicio(reserva.getHoraInicio())
                .horaFin(reserva.getHoraFin())
                
                .build();
    }*/
   
    //--equipamiento -----------
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
                .horaInicioArea(a.getHoraInicioArea())
                .horaFinArea(a.getHoraFinArea())
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




    private LocalTime parseTime(String t) {
        return (t != null && !t.isBlank()) ? LocalTime.parse(t) : null;
    }

    

        /*private CanchaDTO convertToDTO(Cancha c) {
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
    }*/



}
package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.model.Disciplina;
import com.espaciosdeportivos.repository.DisciplinaRepository;
import com.espaciosdeportivos.service.IDisciplinaService;
import com.espaciosdeportivos.service.ImagenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DisciplinaServiceImpl implements IDisciplinaService {
    
    private final DisciplinaRepository disciplinaRepository;
    private final ImagenService imagenService;
    
    private static final String ENTIDAD_TIPO = "DISCIPLINA";
    
    @Override
    @Transactional
    public DisciplinaDTO crearDisciplina(DisciplinaDTO disciplinaDTO) {
        log.info("Creando disciplina: {}", disciplinaDTO.getNombre());

        if (!disciplinaDTO.esValidoParaCreacion()) {
            throw new RuntimeException("Los datos de la disciplina no son válidos");
        }

        if (disciplinaRepository.existsByNombreAndEstadoTrue(disciplinaDTO.getNombre())) {
            throw new RuntimeException("Ya existe una disciplina con el nombre: " + disciplinaDTO.getNombre());
        }

        // Convertir DTO → Entidad
        Disciplina disciplina = convertToEntity(disciplinaDTO);
        disciplina.setEstado(true);
        disciplina = disciplinaRepository.save(disciplina);

        log.info("✅ Disciplina creada con ID: {}", disciplina.getIdDisciplina());

        // Procesar imágenes si existen
        procesarImagenesDisciplina(disciplinaDTO, disciplina.getIdDisciplina());

        return convertDisciplinaToDTO(disciplina);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DisciplinaDTO obtenerDisciplinaPorId(Long idDisciplina) {
        log.info("Obteniendo todas las diciplinas id");
        Disciplina disciplina = disciplinaRepository.findByIdDisciplinaAndEstadoTrue(idDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con id: " + idDisciplina));

        return convertDisciplinaToDTO(disciplina);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaDTO> obtenerTodasLasDisciplinas() {
        log.info("Obteniendo todas las disciplinas activas");
        
        List<Disciplina> disciplinas = disciplinaRepository.findByEstadoTrue();
        return disciplinas.stream()
            .map(this::convertDisciplinaToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaDTO> listarTodas() {
        log.info("Obteniendo todas las disciplinas activas");
        
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        return disciplinas.stream()
            .map(this::convertDisciplinaToDTO)
            .collect(Collectors.toList());
    }
    

    


    @Override
    @Transactional
    public DisciplinaDTO actualizarDisciplina(Long idDisciplina, DisciplinaDTO disciplinaDTO) {
        log.info("Actualizando disciplina ID: {}", idDisciplina);
        
        Disciplina disciplina = disciplinaRepository.findByIdDisciplinaAndEstadoTrue(idDisciplina)
            .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con id: " + idDisciplina));
        
        // Validar nombre único (si cambió)
        if (disciplinaDTO.getNombre() != null && 
            !disciplinaDTO.getNombre().equals(disciplina.getNombre()) && 
            disciplinaRepository.existsByNombreAndIdDisciplinaNotAndEstadoTrue(disciplinaDTO.getNombre(), idDisciplina)) {
            throw new RuntimeException("Ya existe una disciplina con el nombre: " + disciplinaDTO.getNombre());
        }
        
        // Actualizar campos
        if (disciplinaDTO.getNombre() != null) {
            disciplina.setNombre(disciplinaDTO.getNombre());
        }
        if (disciplinaDTO.getDescripcion() != null) {
            disciplina.setDescripcion(disciplinaDTO.getDescripcion());
        }
        if (disciplinaDTO.getEstado() != null) {
            disciplina.setEstado(disciplinaDTO.getEstado());
        }
        
        disciplina = disciplinaRepository.save(disciplina);
        log.info("Disciplina actualizada");
        
        // Procesar nuevas imágenes si existen
        procesarImagenesDisciplina(disciplinaDTO, idDisciplina);
        
        return convertDisciplinaToDTO(disciplina);
    }
    
    @Override
    public void eliminarDisciplinaLogicamente(Long idDisciplina) {
        log.info("Eliminando lógicamente disciplina ID: {}", idDisciplina);
        
        Disciplina disciplina = disciplinaRepository.findById(idDisciplina)
            .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con id: " + idDisciplina));
        disciplina.setEstado(false);
        disciplinaRepository.save(disciplina);
        
        log.info("Disciplina desactivada");
    }

    @Override
    @Transactional
    public DisciplinaDTO eliminar(Long id, Boolean nuevoEstado) {
        log.info("Eliminando lógicamente disciplina ID: {}", id);
    
        Disciplina disciplina = disciplinaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con id: " + id));
        disciplina.setEstado(nuevoEstado);
        return convertDisciplinaToDTO(disciplinaRepository.save(disciplina));
    }
    
    @Override
    @Transactional
    public void eliminarDisciplinaFisicamente(Long idDisciplina) {
        log.info("Eliminando físicamente disciplina ID: {}", idDisciplina);
        
        if (!disciplinaRepository.existsById(idDisciplina)) {
            throw new RuntimeException("Disciplina no encontrada con id: " + idDisciplina);
        }
        
        // Eliminar imágenes primero
        try {
            imagenService.eliminarTodasImagenesDeEntidad(ENTIDAD_TIPO, idDisciplina);
            log.info("Imágenes de la disciplina eliminadas");
        } catch (Exception e) {
            log.warn("Error eliminando imágenes: {}", e.getMessage());
        }
        
        // Eliminar disciplina
        disciplinaRepository.deleteById(idDisciplina);
        log.info("Disciplina eliminada completamente");
    }
    

    
    // ========================== FILTROS Y UTILIDADES ===

    // Métodos adicionales que podrías necesitar
   /*  @Transactional(readOnly = true)
    public boolean existeDisciplinaConNombre(String nombre) {
        return disciplinaRepository.existsByNombreAndEstadoTrue(nombre);
    }*/
    
    @Transactional(readOnly = true)
    public long contarDisciplinasActivas() {
        return disciplinaRepository.countByEstadoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaDTO> buscarPorNombre(String nombre) {
        log.info("🔍 Buscando disciplinas por nombre: {}", nombre);
        
        List<Disciplina> disciplinas = disciplinaRepository.findByNombreContainingIgnoreCase(nombre);
        return disciplinas.stream()
                .map(this::convertDisciplinaToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaDTO> buscarPorDescripcion(String descripcion) {
        log.info("🔍 Buscando disciplinas por descripción: {}", descripcion);
        
        List<Disciplina> disciplinas = disciplinaRepository.buscarPorDescripcion(descripcion);
        return disciplinas.stream()
                .map(this::convertDisciplinaToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaDTO> obtenerDisciplinasInactivas() {
        log.info("📥 Obteniendo disciplinas inactivas");
        
        List<Disciplina> disciplinas = disciplinaRepository.findByEstado(false);
        return disciplinas.stream()
                .map(this::convertDisciplinaToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DisciplinaDTO activarDisciplina(Long idDisciplina) {
        log.info("Activando disciplina ID: {}", idDisciplina);
        
        Disciplina disciplina = disciplinaRepository.findById(idDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada"));
        
        disciplina.setEstado(true);
        Disciplina disciplinaActivada = disciplinaRepository.save(disciplina);
        
        log.info("Disciplina activada: {}", disciplinaActivada.getNombre());
        return convertDisciplinaToDTO(disciplinaActivada);
    }

    @Override
    @Transactional
    public void desactivarMasivo(List<Long> idsDisciplinas) {
        log.info("Desactivando {} disciplinas masivamente", idsDisciplinas.size());
        
        List<Disciplina> disciplinas = disciplinaRepository.findAllById(idsDisciplinas);
        
        for (Disciplina disciplina : disciplinas) {
            disciplina.setEstado(false);
            log.debug("Desactivada: {}", disciplina.getNombre());
        }
        
        disciplinaRepository.saveAll(disciplinas);
        log.info("{} disciplinas desactivadas", disciplinas.size());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarNombreDisponible(String nombre) {
        return !disciplinaRepository.existsByNombreAndEstadoTrue(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarNombreDisponibleParaActualizacion(String nombre, Long idDisciplina) {
        return !disciplinaRepository.existsByNombreAndIdDisciplinaNotAndEstadoTrue(nombre, idDisciplina);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeEliminarse(Long idDisciplina) {
        // Aquí puedes agregar lógica de negocio
        // Por ejemplo, verificar si tiene reservas activas, etc.
        // Por ahora, retornamos true
        return true;
    }

    //OJO IMPLEMENTAR MAS ADELANTE
    /*@Override
    @Transactional(readOnly = true)
    public long contarDisciplinasInactivas() {
        return disciplinaRepository.countByEstado(false);
    }
*/

///REVISAR ESTE
    @Override
    @Transactional(readOnly = true)
    public List<DisciplinaDTO> obtenerRecientes(int limite) {
        log.info("gObteniendo {} disciplinas más recientes", limite);
        
        List<Disciplina> disciplinas = disciplinaRepository.findByEstadoTrueOrderByFechaCreacionDesc();
        
        return disciplinas.stream()
                .limit(limite)
                .map(this::convertDisciplinaToDTO)
                .collect(Collectors.toList());
    }

        // ========== IMAGENES ==========
    @Override
    @Transactional
    public DisciplinaDTO agregarImagenes(Long idDisciplina, List<MultipartFile> archivosImagenes) {
        log.info("Agregando {} imágenes a disciplina ID: {}", archivosImagenes.size(), idDisciplina);
        
        // Verificar que existe
        Disciplina disciplina = disciplinaRepository.findByIdDisciplinaAndEstadoTrue(idDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada o inactiva"));
        
        // Usar servicio de imágenes
        imagenService.guardarImagenesParaEntidad(archivosImagenes, ENTIDAD_TIPO, idDisciplina);
        
        log.info("Imágenes agregadas exitosamente");
        return convertDisciplinaToDTO(disciplina);
    }
    
    private void procesarImagenesDisciplina(DisciplinaDTO disciplinaDTO, Long idDisciplina) {
        if (disciplinaDTO.tieneArchivosParaProcesar()) {
            try {
                imagenService.guardarImagenesParaEntidad(
                    disciplinaDTO.getArchivosImagenes(), 
                    ENTIDAD_TIPO, 
                    idDisciplina
                );
                log.info("{} imágenes procesadas para disciplina", disciplinaDTO.getArchivosImagenes().size());
            } catch (Exception e) {
                log.error("Error procesando imágenes: {}", e.getMessage());
                throw new RuntimeException("Error al procesar las imágenes: " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public DisciplinaDTO eliminarImagen(Long idDisciplina, Long idImagenRelacion) {
        log.info("Eliminando imagen {} de disciplina {}", idImagenRelacion, idDisciplina);
        
        // Verificar que existe la disciplina
        disciplinaRepository.findByIdDisciplinaAndEstadoTrue(idDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada o inactiva"));
        
        // Eliminar usando servicio de imágenes
        imagenService.eliminarImagenLogicamente(idImagenRelacion);
        
        log.info("Imagen eliminada");
        return obtenerDisciplinaPorId(idDisciplina);
    }

    @Override
    @Transactional
    public DisciplinaDTO reordenarImagenes(Long idDisciplina, List<Long> idsImagenesOrden) {
        log.info("Reordenando {} imágenes de disciplina {}", idsImagenesOrden.size(), idDisciplina);
        
        // Verificar que existe
        disciplinaRepository.findByIdDisciplinaAndEstadoTrue(idDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada o inactiva"));
        
        // Reordenar usando servicio de imágenes
        imagenService.reordenarImagenes(ENTIDAD_TIPO, idDisciplina, idsImagenesOrden);
        
        log.info("Imágenes reordenadas");
        return obtenerDisciplinaPorId(idDisciplina);
    }

    private Disciplina convertToEntity(DisciplinaDTO dto) {
        return Disciplina.builder()
                .idDisciplina(dto.getIdDisciplina())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .estado(dto.getEstado())
                .build();
    }

    private DisciplinaDTO convertDisciplinaToDTO(Disciplina disciplina) {

        DisciplinaDTO dto = DisciplinaDTO.builder()
                .idDisciplina(disciplina.getIdDisciplina())
                .nombre(disciplina.getNombre())
                .descripcion(disciplina.getDescripcion())
                .estado(disciplina.getEstado())
                .build();

        try {
            List<ImagenDTO> imagenes = imagenService.obtenerImagenesPorEntidad(ENTIDAD_TIPO, disciplina.getIdDisciplina());
            dto.setImagenes(imagenes);
        } catch (Exception e) {
            dto.setImagenes(List.of());
            log.warn("⚠️ Error cargando imágenes para disciplina {}: {}", disciplina.getIdDisciplina(), e.getMessage());
        }
        return dto;
    }


}
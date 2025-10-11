package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.ImagenRelacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenRelacionRepository extends JpaRepository<ImagenRelacion, Long> {
    
    // Consultas básicas por entidad
    List<ImagenRelacion> findByEntidadTipoAndEntidadIdAndEstadoTrue(String entidadTipo, Long entidadId);
    
    List<ImagenRelacion> findByEntidadTipoAndEntidadId(String entidadTipo, Long entidadId);
    
    Optional<ImagenRelacion> findByImagenIdImagenAndEntidadTipoAndEntidadId(Long idImagen, String entidadTipo, Long entidadId);
    
    // Actualizaciones
    @Modifying
    @Query("UPDATE ImagenRelacion ir SET ir.estado = :estado WHERE ir.idImagenRelacion = :idImagenRelacion")
    void actualizarEstado(@Param("idImagenRelacion") Long idImagenRelacion, @Param("estado") Boolean estado);
    
    @Modifying
    @Query("UPDATE ImagenRelacion ir SET ir.orden = :orden WHERE ir.idImagenRelacion = :idImagenRelacion")
    void actualizarOrden(@Param("idImagenRelacion") Long idImagenRelacion, @Param("orden") Integer orden);
    
    // Consultas ordenadas
    @Query("SELECT ir FROM ImagenRelacion ir WHERE ir.entidadTipo = :entidadTipo AND ir.entidadId = :entidadId ORDER BY ir.orden ASC")
    List<ImagenRelacion> findImagenesConOrden(@Param("entidadTipo") String entidadTipo, @Param("entidadId") Long entidadId);
    
    // Verificaciones y conteo
    boolean existsByEntidadTipoAndEntidadIdAndEstadoTrue(String entidadTipo, Long entidadId);
    
    long countByEntidadTipoAndEntidadIdAndEstadoTrue(String entidadTipo, Long entidadId);
    
    boolean existsByImagenIdImagenAndEntidadTipoAndEntidadIdAndEstadoTrue(Long idImagen, String entidadTipo, Long entidadId);
    
    boolean existsByImagenIdImagenAndEstadoTrue(Long idImagen);
    
    // Consultas por estado
    List<ImagenRelacion> findByEstado(Boolean estado);
    
    List<ImagenRelacion> findByEntidadTipo(String entidadTipo);
    
    List<ImagenRelacion> findByImagenIdImagen(Long idImagen);
    
    List<ImagenRelacion> findByImagenIdImagenAndEstadoTrue(Long idImagen);
    
    // Eliminaciones masivas
    @Modifying
    @Query("DELETE FROM ImagenRelacion ir WHERE ir.entidadTipo = :entidadTipo AND ir.entidadId = :entidadId")
    void deleteByEntidadTipoAndEntidadId(@Param("entidadTipo") String entidadTipo, @Param("entidadId") Long entidadId);
    
    @Modifying
    @Query("UPDATE ImagenRelacion ir SET ir.estado = false WHERE ir.entidadTipo = :entidadTipo AND ir.entidadId = :entidadId")
    void desactivarPorEntidad(@Param("entidadTipo") String entidadTipo, @Param("entidadId") Long entidadId);
    
    // Gestión de órdenes
    @Query("SELECT COALESCE(MAX(ir.orden), 0) FROM ImagenRelacion ir WHERE ir.entidadTipo = :entidadTipo AND ir.entidadId = :entidadId")
    Integer obtenerSiguienteOrden(@Param("entidadTipo") String entidadTipo, @Param("entidadId") Long entidadId);
    
    @Modifying
    @Query("UPDATE ImagenRelacion ir SET ir.orden = ir.orden - 1 WHERE ir.entidadTipo = :entidadTipo AND ir.entidadId = :entidadId AND ir.orden > :ordenEliminado")
    void reordenarDespuesDeEliminar(
        @Param("entidadTipo") String entidadTipo, 
        @Param("entidadId") Long entidadId, 
        @Param("ordenEliminado") Integer ordenEliminado
    );
    
    // Consultas avanzadas con FETCH
    @Query("SELECT ir FROM ImagenRelacion ir JOIN FETCH ir.imagen WHERE ir.entidadTipo = :entidadTipo AND ir.entidadId = :entidadId AND ir.estado = true ORDER BY ir.orden ASC")
    List<ImagenRelacion> findWithImagenByEntidad(
        @Param("entidadTipo") String entidadTipo, 
        @Param("entidadId") Long entidadId
    );
    
    // Estadísticas y reportes
    @Query("SELECT ir.entidadTipo, COUNT(ir) FROM ImagenRelacion ir WHERE ir.estado = true GROUP BY ir.entidadTipo")
    List<Object[]> contarRelacionesPorTipoEntidad();
    
    @Query("SELECT ir.entidadTipo, ir.entidadId FROM ImagenRelacion ir WHERE ir.imagen.idImagen = :idImagen AND ir.estado = true")
    List<Object[]> findEntidadesQueUsanImagen(@Param("idImagen") Long idImagen);
    
    @Query("SELECT COUNT(ir) > 0 FROM ImagenRelacion ir WHERE ir.imagen.idImagen = :idImagen AND ir.estado = true")
    boolean estaImagenEnUso(@Param("idImagen") Long idImagen);
    
    // Detección de duplicados
    @Query("SELECT ir1 FROM ImagenRelacion ir1 WHERE ir1.estado = true AND EXISTS (" +
           "SELECT ir2 FROM ImagenRelacion ir2 WHERE ir2.estado = true AND " +
           "ir2.imagen.idImagen = ir1.imagen.idImagen AND " +
           "ir2.entidadTipo = ir1.entidadTipo AND " +
           "ir2.entidadId = ir1.entidadId AND " +
           "ir2.idImagenRelacion < ir1.idImagenRelacion)")
    List<ImagenRelacion> findRelacionesDuplicadas();
    
    // Consultas por fechas
    @Query("SELECT ir FROM ImagenRelacion ir WHERE ir.fechaCreacion BETWEEN :fechaInicio AND :fechaFin AND ir.estado = true")
    List<ImagenRelacion> findByRangoFechas(
        @Param("fechaInicio") LocalDateTime fechaInicio, 
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    // Validaciones de integridad
    @Query("SELECT ir FROM ImagenRelacion ir WHERE ir.estado = true AND ir.imagen.estado = false")
    List<ImagenRelacion> findRelacionesConImagenInactiva();
    
    @Query("SELECT COUNT(ir) FROM ImagenRelacion ir WHERE ir.estado = true")
    long countRelacionesActivas();
}
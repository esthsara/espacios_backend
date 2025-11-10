package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    
    // Buscar imágenes por estado
    List<Imagen> findByEstadoTrue();
    List<Imagen> findByEstado(Boolean estado);
    
    // Buscar imágenes por tipo MIME
    List<Imagen> findByTipoMimeContainingIgnoreCase(String tipoMime);
    
    // Buscar imágenes con tamaño mayor a un valor
    List<Imagen> findByTamanioBytesGreaterThan(Long tamanioMinimo);
    
    // Buscar imágenes por nombre de archivo
    List<Imagen> findByNombreArchivoContainingIgnoreCase(String nombre);
    
    // Verificar existencia por nombre de archivo
    boolean existsByNombreArchivo(String nombreArchivo);
    
    // Buscar por ruta de almacenamiento
    Optional<Imagen> findByRutaAlmacenamiento(String rutaAlmacenamiento);
    
    // Actualización masiva de estado
    @Modifying
    @Query("UPDATE Imagen i SET i.estado = :estado WHERE i.idImagen = :idImagen")
    void actualizarEstado(@Param("idImagen") Long idImagen, @Param("estado") Boolean estado);
    
    // Contar imágenes por estado
    long countByEstado(Boolean estado);
    
    // Obtener imágenes ordenadas por fecha
    List<Imagen> findByEstadoTrueOrderByFechaCreacionDesc();
    
    // Buscar imágenes creadas después de una fecha
    List<Imagen> findByFechaCreacionAfterAndEstadoTrue(LocalDateTime fecha);
    
    //listar imagenes
    
    // Buscar por múltiples tipos MIME
    @Query("SELECT i FROM Imagen i WHERE i.tipoMime IN :tiposMime AND i.estado = true")
    List<Imagen> findByTiposMime(@Param("tiposMime") List<String> tiposMime);
    
    // Top N imágenes más grandes
    @Query("SELECT i FROM Imagen i WHERE i.estado = true ORDER BY i.tamanioBytes DESC LIMIT :limit")
    List<Imagen> findTopNByTamanioDesc(@Param("limit") int limit);
    
    // Eliminación lógica masiva por IDs
    @Modifying
    @Query("UPDATE Imagen i SET i.estado = false WHERE i.idImagen IN :ids")
    void desactivarPorIds(@Param("ids") List<Long> ids);
}
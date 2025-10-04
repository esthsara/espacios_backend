package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Buscar comentarios por ID
    List<Comentario> findByPersona_Id(Long id);

    // Buscar comentarios por ID de cancha
    List<Comentario> findByCancha_IdCancha(Long idCancha);

    // Buscar comentarios con calificación mayor o igual a cierto valor
    List<Comentario> findByCalificacionGreaterThanEqual(Integer calificacion);

    // Buscar comentarios recientes
    List<Comentario> findAllByOrderByFechaDesc();
}
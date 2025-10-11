package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    
    // Métodos existentes (los mantienes)
    Optional<Disciplina> findByIdDisciplinaAndEstadoTrue(Long idDisciplina);
    List<Disciplina> findByEstadoTrue();
    boolean existsByNombreAndEstadoTrue(String nombre);
    
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Disciplina d WHERE d.nombre = :nombre AND d.idDisciplina != :idDisciplina AND d.estado = true")
    boolean existsByNombreAndIdDisciplinaNotAndEstadoTrue(@Param("nombre") String nombre, @Param("idDisciplina") Long idDisciplina);
    
    // Nuevos métodos útiles
    Optional<Disciplina> findByNombre(String nombre);
    List<Disciplina> findByNombreContainingIgnoreCase(String nombre);
    List<Disciplina> findByEstado(Boolean estado);
    boolean existsByNombre(String nombre);
    
    @Query("SELECT d FROM Disciplina d WHERE LOWER(d.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')) AND d.estado = true")
    List<Disciplina> buscarPorDescripcion(@Param("descripcion") String descripcion);
    
    long countByEstadoTrue();
    List<Disciplina> findByFechaCreacionAfterAndEstadoTrue(LocalDateTime fecha);
    //REVISAR SI FUNCIONAa
    List<Disciplina>findByEstadoTrueOrderByFechaCreacionDesc();
}
package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Disciplina;
import com.espaciosdeportivos.model.dispone;
import com.espaciosdeportivos.model.sepractica;
import com.espaciosdeportivos.model.sepracticaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface sepracticaRepository extends JpaRepository<sepractica, sepracticaId> {

    List<sepractica> findById_IdCancha(Long idCancha);

    List<sepractica> findByCanchaIdCancha(Long idCancha);// agregue esta

    List<sepractica> findByDisciplina_IdDisciplina(Long idDisciplina);

    Optional<sepractica> findById_IdCanchaAndId_IdDisciplina(Long idCancha, Long idDisciplina);

    boolean existsById_IdCanchaAndId_IdDisciplina(Long idCancha, Long idDisciplina);

    void deleteById_IdCanchaAndId_IdDisciplina(Long idCancha, Long idDisciplina);

    /*
     * @Query("SELECT COUNT(s) > 0 FROM SePractica s " +
     * "WHERE s.id.idCancha = :idCancha " +
     * "AND s.id.idDisciplina = :idDisciplina")
     * boolean existeDisciplinaEnCancha(
     * 
     * @Param("idCancha") Long idCancha,
     * 
     * @Param("idDisciplina") Long idDisciplina
     * );
     */

    @Query("SELECT s FROM sepractica s WHERE s.id.idCancha = :idCancha")
    List<sepractica> obtenerPorCancha(@Param("idCancha") Long idCancha);

    @Query("SELECT s.disciplina FROM sepractica s WHERE s.id.idCancha = :idCancha")
    List<Disciplina> obtenerDisciplinasPorCancha(@Param("idCancha") Long idCancha);

}
package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.sepractica;
import com.espaciosdeportivos.model.sepracticaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface sepracticaRepository extends JpaRepository<sepractica, sepracticaId> {

    List<sepractica> findById_IdCancha(Long idCancha);

    List<sepractica> findByDisciplina_IdDisciplina(Long idDisciplina);

    Optional<sepractica> findById_IdCanchaAndId_IdDisciplina(Long idCancha, Long idDisciplina);

    boolean existsById_IdCanchaAndId_IdDisciplina(Long idCancha, Long idDisciplina);

    void deleteById_IdCanchaAndId_IdDisciplina(Long idCancha, Long idDisciplina);
}
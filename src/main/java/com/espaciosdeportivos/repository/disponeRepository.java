package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.dispone;
import com.espaciosdeportivos.model.disponeId;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface disponeRepository extends JpaRepository<dispone, disponeId> {

    @EntityGraph(attributePaths = {"cancha", "equipamiento"})
    List<dispone> findById_IdCancha(Long idCancha);

    @EntityGraph(attributePaths = {"cancha", "equipamiento"})
    List<dispone> findByEquipamiento_IdEquipamiento(Long idEquipamiento);   

    @EntityGraph(attributePaths = {"cancha", "equipamiento"})
    Optional<dispone> findById_IdCanchaAndId_IdEquipamiento(Long idCancha, Long idEquipamiento);

    boolean existsById_IdCanchaAndId_IdEquipamiento(Long idCancha, Long idEquipamiento);

    void deleteById_IdCanchaAndId_IdEquipamiento(Long idCancha, Long idEquipamiento);

    List<dispone> findByCanchaIdCancha(Long idCancha);
}

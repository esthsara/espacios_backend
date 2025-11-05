package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Dispone;
import com.espaciosdeportivos.model.DisponeId;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface disponeRepository extends JpaRepository<Dispone, DisponeId> {

    @EntityGraph(attributePaths = {"cancha", "equipamiento"})
    List<Dispone> findById_IdCancha(Long idCancha);

    @EntityGraph(attributePaths = {"cancha", "equipamiento"})
    List<Dispone> findByEquipamiento_IdEquipamiento(Long idEquipamiento);   

    @EntityGraph(attributePaths = {"cancha", "equipamiento"})
    Optional<Dispone> findById_IdCanchaAndId_IdEquipamiento(Long idCancha, Long idEquipamiento);

    boolean existsById_IdCanchaAndId_IdEquipamiento(Long idCancha, Long idEquipamiento);

    void deleteById_IdCanchaAndId_IdEquipamiento(Long idCancha, Long idEquipamiento);

    List<Dispone> findByCanchaIdCancha(Long idCancha); //aqui se agrego

}

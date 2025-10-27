package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.supervisa;
import com.espaciosdeportivos.model.supervisaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface supervisaRepository extends JpaRepository<supervisa, supervisaId> {

    // Ver todas las canchas que supervisa un usuario
    List<supervisa> findById_IdUsControl(Long idUsuarioControl);

    // Ver todos los usuarios que supervisan una cancha
    List<supervisa> findById_IdCancha(Long idCancha);

    // Buscar una relación específica
    Optional<supervisa> findById_IdUsControlAndId_IdCancha(Long idUsuarioControl, Long idCancha);

    // Verificar si ya existe la relación
    boolean existsById_IdUsControlAndId_IdCancha(Long idUsuarioControl, Long idCancha);

    // Eliminar la relación
    void deleteById_IdUsControlAndId_IdCancha(Long idUsuarioControl, Long idCancha);
}

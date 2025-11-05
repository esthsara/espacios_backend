package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.UsuarioControl;
import com.espaciosdeportivos.model.Supervisa;
import com.espaciosdeportivos.model.SupervisaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupervisaRepository extends JpaRepository<Supervisa, SupervisaId> {

    // Ver todas las canchas que supervisa un usuario
    List<Supervisa> findById_IdUsControl(Long idUsuarioControl);

    // Ver todos los usuarios que supervisan una cancha
    List<Supervisa> findById_IdCancha(Long idCancha);

    // Buscar una relación específica
    Optional<Supervisa> findById_IdUsControlAndId_IdCancha(Long idUsuarioControl, Long idCancha);

    // Verificar si ya existe la relación
    boolean existsById_IdUsControlAndId_IdCancha(Long idUsuarioControl, Long idCancha);

    // Eliminar la relación
    void deleteById_IdUsControlAndId_IdCancha(Long idUsuarioControl, Long idCancha);

    // Obtener todos los usuarios de control 
    // que pertenezcan a canchas de áreas deportivas de un administrador específico
    @Query("SELECT DISTINCT s.usuarioControl FROM Supervisa s " +
       "JOIN s.cancha c " +
       "JOIN c.areaDeportiva a " +
       "WHERE a.administrador.id = :idAdmin")
    List<UsuarioControl> findUsuariosControlByAdministradorId(@Param("idAdmin") Long idAdmin);


}

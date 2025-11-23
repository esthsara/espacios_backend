package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.incluye;
import com.espaciosdeportivos.model.incluyeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface incluyeRepository extends JpaRepository<incluye, incluyeId> {
    Optional<incluye> findByReservaIdReserva(Long idReserva);

    List<incluye> findByCanchaIdCancha(Long idCancha);

    List<incluye> findByDisciplinaIdDisciplina(Long idDisciplina);

    @Query("SELECT i FROM incluye i WHERE i.id.idReserva = :idReserva")
    List<incluye> findByIdReserva(@Param("idReserva") Long idReserva);

    @Query("SELECT i FROM incluye i WHERE i.id.idCancha = :idCancha")
    List<incluye> findByIdCancha(@Param("idCancha") Long idCancha);

    @Query("SELECT i FROM incluye i WHERE i.id.idDisciplina = :idDisciplina")
    List<incluye> findByIdDisciplina(@Param("idDisciplina") Long idDisciplina);

    // Optional: si necesitas búsqueda por fecha + cancha (para disponibilidad)
    @Query("SELECT i FROM incluye i WHERE i.id.idCancha = :idCancha AND i.reserva.fechaReserva = :fecha")
    List<incluye> findByCanchaAndFecha(@Param("idCancha") Long idCancha, @Param("fecha") java.time.LocalDate fecha);

}

package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Incluye;
import com.espaciosdeportivos.model.IncluyeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public  interface IncluyeRepository extends JpaRepository<Incluye, IncluyeId>{
    Optional<Incluye> findByReservaIdReserva(Long idReserva);

    List<Incluye> findByCanchaIdCancha(Long idCancha);

    List<Incluye> findByDisciplinaIdDisciplina(Long idDisciplina);


    @Query("SELECT i FROM Incluye i WHERE i.id.idReserva = :idReserva")
    List<Incluye> findByIdReserva(@Param("idReserva") Long idReserva);

    @Query("SELECT i FROM Incluye i WHERE i.id.idCancha = :idCancha")
    List<Incluye> findByIdCancha(@Param("idCancha") Long idCancha);

    @Query("SELECT i FROM Incluye i WHERE i.id.idDisciplina = :idDisciplina")
    List<Incluye> findByIdDisciplina(@Param("idDisciplina") Long idDisciplina);

    // Optional: si necesitas búsqueda por fecha + cancha (para disponibilidad)
    @Query("SELECT i FROM Incluye i WHERE i.id.idCancha = :idCancha AND i.reserva.fechaReserva = :fecha")
    List<Incluye> findByCanchaAndFecha(@Param("idCancha") Long idCancha, @Param("fecha") java.time.LocalDate fecha);

}


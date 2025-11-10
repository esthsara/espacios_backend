package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Cancha;
import com.espaciosdeportivos.model.Cliente;
import com.espaciosdeportivos.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Búsquedas por Cliente (usando id_persona)
    List<Reserva> findByClienteId(Long clienteId);
    List<Reserva> findByClienteIdAndEstadoReserva(Long clienteId, String estadoReserva);
    
    // Búsquedas por Estado
    List<Reserva> findByEstadoReserva(String estadoReserva);
    
    // Búsquedas por Fecha
    List<Reserva> findByFechaReserva(LocalDate fechaReserva);
    List<Reserva> findByFechaReservaBetween(LocalDate inicio, LocalDate fin);
    List<Reserva> findByFechaReservaBetweenAndEstadoReserva(LocalDate inicio, LocalDate fin, String estadoReserva);
    
    // Búsquedas por Código
    //Optional<Reserva> findByCodigoReserva(String codigoReserva);
    //boolean existsByCodigoReserva(String codigoReserva);
    
    // Consultas de disponibilidad
    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva = :fecha AND " +
           "((r.horaInicio < :horaFin AND r.horaFin > :horaInicio) OR " +
           "(r.horaInicio = :horaInicio AND r.horaFin = :horaFin)) AND " +
           "r.estadoReserva IN ('CONFIRMADA', 'EN_CURSO')")
    List<Reserva> findReservasSolapadas(@Param("fecha") LocalDate fecha,
                                       @Param("horaInicio") LocalTime horaInicio,
                                       @Param("horaFin") LocalTime horaFin);

    // Consultas para reportes
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.fechaReserva = :fecha AND r.estadoReserva = 'CONFIRMADA'")
    Long countReservasConfirmadasPorFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva = :fecha AND " +
           "r.estadoReserva = 'CONFIRMADA' ORDER BY r.horaInicio")
    List<Reserva> findReservasConfirmadasDelDia(@Param("fecha") LocalDate fecha);

    /*@Query("SELECT SUM(r.montoTotal) FROM Reserva r WHERE r.fechaReserva BETWEEN :inicio AND :fin " +
           "AND r.estadoReserva = 'COMPLETADA'")
    Double calcularIngresosEnRango(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);*/

    // Reservas activas del cliente (futuras o en curso hoy)
    @Query("SELECT r FROM Reserva r WHERE r.cliente.id = :clienteId " +
           "AND r.estadoReserva IN ('CONFIRMADA', 'EN_CURSO') " +
           "AND (r.fechaReserva > CURRENT_DATE OR " +
           "(r.fechaReserva = CURRENT_DATE AND r.horaFin > CURRENT_TIME))")
    List<Reserva> findReservasActivasDelCliente(@Param("clienteId") Long clienteId);

    // Reservas próximas (próximos 7 días)
    /*@Query("SELECT r FROM Reserva r WHERE r.fechaReserva BETWEEN CURRENT_DATE AND CURRENT_DATE + 7 " +
           "AND r.estadoReserva = 'CONFIRMADA' ORDER BY r.fechaReserva, r.horaInicio")
    List<Reserva> findReservasProximas();*/

    // Validaciones
    boolean existsByClienteIdAndFechaReserva(Long clienteId, LocalDate fechaReserva);
    long countByFechaReserva(LocalDate fechaReserva);

    //List<Reserva> findReservaByCancha(Long idCancha);

    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva = :fecha AND r.idReserva IN " +
       "(SELECT i.reserva.idReserva FROM Incluye i WHERE i.cancha.idCancha = :idCancha)")
    List<Reserva> findByCanchaAndFecha(@Param("idCancha") Long idCancha, @Param("fecha") LocalDate fecha);

      /* @Query("""
              SELECT r
              FROM Reserva r
              WHERE r.fechaReserva = :fecha
              AND r.idReserva IN (
              SELECT i.reserva.idReserva
              FROM Incluye i
              WHERE i.cancha.idCancha = :idCancha
              )
              """)
       List<Reserva> findByCanchaAndFechaReserva(
              @Param("idCancha") Long idCancha,
              @Param("fecha") LocalDate fecha
       );*/

       // Obtener todos los clientes que han hecho reservas
       // en canchas de áreas deportivas de un administrador específico K
       @Query("""
              SELECT DISTINCT r.cliente
              FROM Reserva r
              JOIN Incluye i ON i.reserva.id = r.id
              JOIN Cancha c ON c.id = i.cancha.id
              JOIN AreaDeportiva a ON a.id = c.areaDeportiva.id
              WHERE a.administrador.id = :idAdmin
              """)
              List<Cliente> findClientesByAdministrador(@Param("idAdmin") Long idAdmin);

}
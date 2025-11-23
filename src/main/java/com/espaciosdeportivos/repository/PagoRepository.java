package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Búsquedas básicas
    List<Pago> findByEstado(String estado);
    List<Pago> findByMetodoPago(String metodoPago);
    List<Pago> findByTipoPago(String tipoPago);
    List<Pago> findByFecha(LocalDate fecha);
    List<Pago> findByFechaBetween(LocalDate inicio, LocalDate fin);
    
    // Búsquedas por Reserva
    List<Pago> findByReservaIdReserva(Long idReserva);
    List<Pago> findByReservaIdReservaAndEstado(Long idReserva, String estado);
    
    // Búsquedas por Cliente
    @Query("SELECT p FROM Pago p WHERE p.reserva.cliente.id = :clienteId")
    List<Pago> findByClienteId(@Param("clienteId") Long clienteId);
    
    @Query("SELECT p FROM Pago p WHERE p.reserva.cliente.id = :clienteId AND p.estado = :estado")
    List<Pago> findByClienteIdAndEstado(@Param("clienteId") Long clienteId, @Param("estado") String estado);

    // Búsquedas por código único
    Optional<Pago> findByCodigoTransaccion(String codigoTransaccion);
    boolean existsByCodigoTransaccion(String codigoTransaccion);
    
    // Validaciones
    boolean existsByMontoAndReservaIdReserva(Double monto, Long idReserva);
    
    // Consultas para reportes
    @Query("SELECT p FROM Pago p WHERE p.fecha BETWEEN :startDate AND :endDate AND p.estado = 'CONFIRMADO'")
    List<Pago> findPagosConfirmadosEnRango(@Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fecha = :fecha AND p.estado = 'CONFIRMADO'")
    Double sumMontoTotalPorFecha(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fecha BETWEEN :inicio AND :fin AND p.estado = 'CONFIRMADO'")
    Double sumMontoTotalPorRango(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
    
    // Consulta para saldo pendiente
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.reserva.idReserva = :idReserva AND p.estado = 'CONFIRMADO'")
    Double sumMontoConfirmadoPorReserva(@Param("idReserva") Long idReserva);
}
package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "tipo_pago", nullable = false, length = 50)
    private String tipoPago;

    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(name = "codigo_transaccion", unique = true)
    private String codigoTransaccion;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    // Auditoría
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relación con Reserva
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    // Enums para validación
    public enum EstadoPago {
        PENDIENTE, CONFIRMADO, ANULADO, RECHAZADO
    }

    public enum MetodoPago {
        EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA, QR
    }

    public enum TipoPago {
        PARCIAL, TOTAL, ANTICIPO
    }

    // Métodos de negocio
    public boolean estaConfirmado() {
        return EstadoPago.CONFIRMADO.name().equals(this.estado);
    }

    public boolean esModificable() {
        return EstadoPago.PENDIENTE.name().equals(this.estado);
    }
}
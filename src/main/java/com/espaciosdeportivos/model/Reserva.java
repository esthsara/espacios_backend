package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "estado_reserva", nullable = false, length = 50)
    private String estadoReserva;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "codigo_reserva", unique = true)
    private String codigoReserva;

    // Auditoría
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // RELACIÓN CON CLIENTE - OPCIÓN A
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_persona", nullable = false)
    private Cliente cliente;

    // Relaciones
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<Pago> pagos;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<Qr> codigosQr;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<incluye> canchasIncluidas;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<participa> invitados;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Cancelacion cancelacion;

    // Enums para estados
    public enum EstadoReserva {
        PENDIENTE, CONFIRMADA, EN_CURSO, COMPLETADA, CANCELADA, NO_SHOW
    }

    // Método para calcular duración
    @PrePersist
    @PreUpdate
    public void calcularDuracion() {
        if (horaInicio != null && horaFin != null) {
            this.duracionMinutos = (int) java.time.Duration.between(horaInicio, horaFin).toMinutes();
        }
    }

    // Métodos de negocio
    public boolean estaActiva() {
        return estadoReserva.equals(EstadoReserva.CONFIRMADA.name()) || 
               estadoReserva.equals(EstadoReserva.EN_CURSO.name());
    }

    public boolean esModificable() {
        return estadoReserva.equals(EstadoReserva.PENDIENTE.name()) || 
               estadoReserva.equals(EstadoReserva.CONFIRMADA.name());
    }

    public boolean puedeCancelarse() {
        return !estadoReserva.equals(EstadoReserva.CANCELADA.name()) && 
               !estadoReserva.equals(EstadoReserva.COMPLETADA.name());
    }
}
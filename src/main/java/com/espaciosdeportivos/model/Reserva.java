package com.espaciosdeportivos.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMPORTANTE
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.time.Duration;

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

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "total_pagado")
    private Double totalPagado;

    @Column(name = "saldo_pendiente")
    private Double saldoPendiente;

    @Column(name = "pagada_completa")
    private Boolean pagadaCompleta;

    // RELACIÓNES

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_persona", nullable = false)
    // Nota: Aquí NO ponemos JsonIgnore porque queremos saber de quién es la reserva
    private Cliente cliente;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // Nota: Aquí NO ponemos JsonIgnore porque queremos saber qué cancha incluye
    private List<incluye> Incluidos;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- Recomendado ignorar pagos al listar reservas masivamente
    private List<Pago> pagos;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- Recomendado ignorar QRs
    private List<Qr> qr;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- Recomendado ignorar invitados
    private List<participa> invitados;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- Recomendado ignorar cancelación
    private Cancelacion cancelacion;

    // ... resto de tus métodos (puedeReprogramar, etc) siguen igual ...
    public enum EstadoReserva {
        PENDIENTE, CONFIRMADA, EN_CURSO, COMPLETADA, CANCELADA, NO_SHOW
    }

    public boolean puedeReprogramar() {
        LocalDateTime inicioReserva = fechaReserva.atTime(horaInicio);
        return Duration.between(LocalDateTime.now(), inicioReserva).toHours() >= 8;
    }

    public boolean puedeCancelar() {
        LocalDateTime inicioReserva = fechaReserva.atTime(horaInicio);
        return Duration.between(LocalDateTime.now(), inicioReserva).toHours() >= 12;
    }

    public boolean estaConfirmada() {
        return EstadoReserva.CONFIRMADA.name().equals(estadoReserva);
    }

    public boolean estaCancelada() {
        return EstadoReserva.CANCELADA.name().equals(estadoReserva);
    }

    public Cancha getCancha() {
        return Incluidos != null && !Incluidos.isEmpty() ? Incluidos.get(0).getCancha() : null;
    }

    @PrePersist
    @PreUpdate
    public void calcularDuracion() {
        if (horaInicio != null && horaFin != null) {
            this.duracionMinutos = (int) java.time.Duration.between(horaInicio, horaFin).toMinutes();
        }
    }

    public boolean estaActiva() {
        return estadoReserva.equals(EstadoReserva.CONFIRMADA.name())
                || estadoReserva.equals(EstadoReserva.EN_CURSO.name());
    }

    public boolean esModificable() {
        return estadoReserva.equals(EstadoReserva.PENDIENTE.name())
                || estadoReserva.equals(EstadoReserva.CONFIRMADA.name());
    }

    public boolean puedeCancelarse() {
        return !estadoReserva.equals(EstadoReserva.CANCELADA.name())
                && !estadoReserva.equals(EstadoReserva.COMPLETADA.name());
    }
}
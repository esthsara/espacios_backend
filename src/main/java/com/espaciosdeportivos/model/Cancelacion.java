package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "cancelacion")
public class Cancelacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancelacion")
    private Long idCancelacion;

    @Column(name = "fecha_cancelacion", nullable = false)
    private LocalDate fechaCancelacion;

    @Column(name = "hora_cancelacion", nullable = false)
    private LocalTime horaCancelacion;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    // (única cancelación por reserva)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    // Cliente que realizó la cancelación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_persona", nullable = false)
    private Cliente cliente;
}

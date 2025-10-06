package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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
    private String tipoPago;   // Ej: "parcial", "total"

    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago; // Ej: "efectivo", "tarjeta"

    @Column(nullable = false, length = 30)
    private String estado;     // Ej: "pendiente", "confirmado", "anulado"

    // Relación con Reserva (muchos pagos pueden pertenecer a una reserva)
    @ManyToOne
    @JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva",nullable = false)
    private Reserva reserva;
}

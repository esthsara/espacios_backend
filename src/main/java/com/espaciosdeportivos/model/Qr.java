package com.espaciosdeportivos.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "qr")
public class Qr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_qr")
    private Long idQr;

    @Column(name = "codigo_qr", nullable = false, unique = true, length = 200)
    private String codigoQr;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_persona", nullable = false)
    private Persona usuarioControl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_invitado", referencedColumnName = "id_persona", nullable = false)
    private Persona invitado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva", nullable = false)
    private Reserva reserva;
}

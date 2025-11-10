package com.espaciosdeportivos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"invitado", "reserva"}) // Evita recursividad en toString
@Entity
@Table(name = "participa", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_invitado", "id_reserva"})
       })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Participa {
    
    @EmbeddedId
    private ParticipaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idInvitado")
    @JoinColumn(name = "id_invitado", referencedColumnName = "id_persona", nullable = false)
    private Invitado invitado;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idReserva")
    @JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva", nullable = false)
    private Reserva reserva;

    // Campos adicionales para la relación
    @CreationTimestamp
    @Column(name = "fecha_invitacion", nullable = false, updatable = false)
    private LocalDateTime fechaInvitacion;

    @Column(name = "asistio")
    private Boolean asistio;

    @Column(name = "confirmado")
    private Boolean confirmado;

    @Column(name = "notificado")
    private Boolean notificado;

    @Column(name = "observaciones", length = 300)
    private String observaciones;

    // Métodos de negocio
    public boolean estaConfirmado() {
        return Boolean.TRUE.equals(confirmado);
    }

    public boolean asistio() {
        return Boolean.TRUE.equals(asistio);
    }

    public boolean fueNotificado() {
        return Boolean.TRUE.equals(notificado);
    }

    // Método helper para crear la clave embebida
    public static Participa crear(Invitado invitado, Reserva reserva) {
        ParticipaId id = new ParticipaId(invitado.getId(), reserva.getIdReserva());
        return Participa.builder()
                .id(id)
                .invitado(invitado)
                .reserva(reserva)
                .asistio(false)
                .confirmado(false)
                .notificado(false)
                .build();
    }
}
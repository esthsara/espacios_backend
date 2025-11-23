package com.espaciosdeportivos.model;

import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "usuario_control")
@EqualsAndHashCode(callSuper = true)
public class UsuarioControl extends Persona {

    public UsuarioControl(Long id) {
        super.setId(id); //
    }

    //@NotNull
    @Column(name = "estado_operativo", nullable = false, length = 100)
    private String estadoOperativo; // "Activo", "En turno", "Fuera de servicio"

    //@NotNull
    @Column(name = "hora_inicio_turno", nullable = false)
    private LocalTime horaInicioTurno;

    //@NotNull
    @Column(name = "hora_fin_turno", nullable = false)
    private LocalTime horaFinTurno;

    //@NotNull
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    
    @OneToMany(mappedBy = "usuarioControl", /*cascade = CascadeType.ALL, */orphanRemoval = true)
    private List<Qr> qr; 
}

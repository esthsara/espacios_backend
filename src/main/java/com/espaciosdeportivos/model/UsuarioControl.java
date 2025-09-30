package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
//import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "usuario_control")
@EqualsAndHashCode(callSuper = true)
//@PrimaryKeyJoinColumn(name = "id_us_control")

public class UsuarioControl extends Persona {

    public UsuarioControl(Long id) {
        super.setId(id);
    }

    @NotNull
    @Column(name = "estado_operativo", nullable = false, length = 50)
    private String estadoOperativo;

    @NotNull
    @Column(name = "hora_inicio_turno", nullable = false)
    private LocalTime horaInicioTurno;

    @NotNull
    @Column(name = "hora_fin_turno", nullable = false)
    private LocalTime horaFinTurno;

    @NotNull
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    
    //K
    /*@OneToMany(mappedBy = "usuarioControl", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Qr> qr;*/


}
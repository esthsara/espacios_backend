package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
//import java.util.List;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "administrador")
//@PrimaryKeyJoinColumn(name = "id_administrador")
@EqualsAndHashCode(callSuper = true)
public class Administrador extends Persona {

    public Administrador(Long idPersona) {
        super.setIdPersona(idPersona);
    }

    @NotNull
    @Column(name = "cargo", nullable = false, length = 100)
    private String cargo;

    @NotNull
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    /*@OneToMany(mappedBy = "administrador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaDeportiva> areaDeportiva;*/
}
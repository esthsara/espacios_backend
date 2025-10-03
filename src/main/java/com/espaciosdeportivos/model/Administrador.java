package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "administrador")
@EqualsAndHashCode(callSuper = true)
public class Administrador extends Persona {

    public Administrador(Long id) {
        super.setId(id);
    }

    @NotNull
    @Column(name = "cargo", nullable = false, length = 100)
    private String cargo;

    @NotNull
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @OneToMany(mappedBy = "administrador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaDeportiva> areaDeportiva;

}

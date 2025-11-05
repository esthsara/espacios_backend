package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
//import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cliente")
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Persona {

    public Cliente(Long id) {
        super.setId(id);
    }

    //@NotNull
    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    // revisar sara
    @OneToMany(mappedBy = "cliente", /*cascade = CascadeType.ALL,*/ orphanRemoval = false,fetch = FetchType.LAZY)
    private List<Cancelacion> cancelacion;
}
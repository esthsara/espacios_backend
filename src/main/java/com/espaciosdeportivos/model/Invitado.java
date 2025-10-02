package com.espaciosdeportivos.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "invitado")
@EqualsAndHashCode(callSuper = true)
public class Invitado extends Persona {

    public Invitado(Long id) {
        super.setId(id);
    }

    @NotNull
    @Column(name = "verificado", nullable = false)
    private Boolean verificado;

     //k
    /*@OneToMany(mappedBy = "invitado", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<Qr> qr;*/
}




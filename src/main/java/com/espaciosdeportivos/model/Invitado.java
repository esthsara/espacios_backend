package com.espaciosdeportivos.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
import java.util.List;

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

    //@NotNull
    @Column(name = "verificado", nullable = false)
    private Boolean verificado;

    //aqui ya no va la rrlacion si no con persona directamente 
    /*@OneToMany(mappedBy = "invitado", cascade = CascadeType.ALL,orphanRemoval = true) 
    private List<Qr> qr;*/
}




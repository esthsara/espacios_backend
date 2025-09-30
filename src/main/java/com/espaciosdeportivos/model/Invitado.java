package com.espaciosdeportivos.model;

//import java.util.List;
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
//@PrimaryKeyJoinColumn(name = "id_invitado")
@EqualsAndHashCode(callSuper = true)
public class Invitado extends Persona {

    public Invitado(Long idPersona) {
        super.setIdPersona(idPersona);
    }

    @NotNull
    @Column(name = "verificado", nullable = false)
    private Boolean verificado;

     //k
    /*@OneToMany(mappedBy = "invitado", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<Qr> qr;*/
}
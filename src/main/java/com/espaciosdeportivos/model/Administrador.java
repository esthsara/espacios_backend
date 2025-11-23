package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.validation.constraints.NotNull;
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

    //@NotNull
    @Column(name = "cargo", nullable = false, length = 100)
    private String cargo;

    //@NotNull
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;
    //comentario: no podemos hacer una casacada por que si no existe admin borrra areas loq ue no es correcto
    //comentario : no podemos usar validaciones aqui no es ocrecto
    @OneToMany(mappedBy = "administrador"/*, cascade = CascadeType.ALL*/, orphanRemoval = false ,fetch = FetchType.LAZY)
    @JsonIgnore 
    private List<AreaDeportiva> areaDeportiva;

}


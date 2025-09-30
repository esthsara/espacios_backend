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
@Table(name = "cliente")
//@PrimaryKeyJoinColumn(name = "id_cliente")
@EqualsAndHashCode(callSuper = true)

public class Cliente extends Persona {

    public Cliente(Long id) {
        super.setId(id);
    }

    @NotNull
    @Column(name = "estado_cliente", nullable = false, length = 50)
    private String estadoCliente;

    //k
    /*@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Cancelacion> cancelacion;*/
}
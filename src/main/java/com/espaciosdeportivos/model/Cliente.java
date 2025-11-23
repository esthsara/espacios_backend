package com.espaciosdeportivos.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMPORTANTE
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
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

    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @OneToMany(mappedBy = "cliente", orphanRemoval = false, fetch = FetchType.LAZY)
    @JsonIgnore // <--- EVITA EL BUCLE Y ERROR LAZY
    private List<Cancelacion> cancelacion;

    @OneToMany(mappedBy = "cliente", orphanRemoval = false, fetch = FetchType.LAZY)
    @JsonIgnore // <--- EVITA EL BUCLE Y ERROR LAZY
    private List<Pago> pagos;
}
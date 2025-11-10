package com.espaciosdeportivos.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "macrodistrito")
public class Macrodistrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_macrodistrito")
    private Long idMacrodistrito;

    @Column(name = "nombre_macrodistrito", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", length = 600)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    //Si guardo, actualizas o eliminas un Macrodistrito esas operaciones se propagan automáticamente a susZona
    //si elimino un macroitrito se eliminan sus zonas asociadas

    @OneToMany(mappedBy = "macrodistrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zona> zona;
    
}

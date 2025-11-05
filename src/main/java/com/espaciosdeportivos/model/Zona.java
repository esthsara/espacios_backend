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
@Table(name = "zona")
public class Zona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Long idZona;

    @Column(name = "nombre_zona", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "descripcion", length = 600)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_macrodistrito")
    private Macrodistrito macrodistrito;

    //comentario: Nose si es correcto que al eliminar zona se eliinara el area deportiva por eso quite el cascadetypeall
    //comentario : le puse false por que si elimino zona eliminaria tambien areadeportiva
    @OneToMany(mappedBy = "zona", /*cascade = CascadeType.ALL ,*/orphanRemoval = false)
    private List<AreaDeportiva> areaDeportiva;
    
}
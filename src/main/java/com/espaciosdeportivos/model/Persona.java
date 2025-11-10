package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

//import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    //@NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    //@NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "a_paterno", nullable = true)
    private String apellidoPaterno;

    @Column(name = "a_materno", nullable = false)
    private String apellidoMaterno;

    //@NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Email
    //@NotNull
    @Column(name = "email", nullable = false)
    private String email;

    //@NotNull
    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    ///@NotNull
    @Column(name = "estado", nullable = false)
    private Boolean estado;
    //use Lazi por que Solo carga esta relación cuando yo la pida explícitamente en el código."
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Comentario> comentario;

}

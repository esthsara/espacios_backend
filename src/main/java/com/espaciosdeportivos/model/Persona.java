package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)  // Este es el tipo de herencia que usas
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "a_paterno")
    private String aPaterno;

    @Column(name = "a_materno")
    private String aMaterno;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Email
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    @NotNull
    @Column(name = "estado", nullable = false, length = 20)
    private Boolean estado;
}

package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "app_user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(unique=true, nullable=false)
    private String email;

    @OneToOne
    @JoinColumn(name = "persona_id")
    private com.espaciosdeportivos.model.Persona persona;
    //nohemy revisa bien esto y preguntale a la ia si esta bien asi
    //dice que elimines el email de persona y solo uses el depersona y lo mismo con activo

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private Boolean activo = false; // solo verdadero si admin aprueba

    @Column(name = "estado_verificacion", nullable = false)
    private String estadoVerificacion = "PENDIENTE"; // PENDIENTE, APROBADO, RECHAZADO

    @Column(name = "rol_solicitado")
    private String rolSolicitado; // "CLIENTE", "ADMINISTRADOR", "SUPERUSUARIO"
}

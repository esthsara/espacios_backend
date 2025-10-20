package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, unique = true, nullable = false)
    private RoleName name;

    public enum RoleName {
        ROL_SUPERUSUARIO,
        ROL_ADMINISTRADOR,
        ROL_CLIENTE
    }
}

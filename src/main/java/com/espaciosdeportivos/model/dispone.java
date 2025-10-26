package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "dispone") 
public class Dispone {
    
    @EmbeddedId
    private DisponeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCancha")
    @JoinColumn(name = "id_cancha", referencedColumnName = "id_cancha", insertable = false, updatable = false)
    private Cancha cancha;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idEquipamiento")
    @JoinColumn(name = "id_equipamiento", referencedColumnName = "id_equipamiento", insertable = false, updatable = false)
    private Equipamiento equipamiento;

    @Column(name = "cantidad",nullable = false)
    private Integer cantidad;
}
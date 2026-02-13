package com.horizonx.recruiting_microservices.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un barrio.
 */
@Entity
@Table(name = "barrios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barrio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id", nullable = false)
    private Ciudad ciudad;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "barrio", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Candidato> candidatos = new ArrayList<>();
}

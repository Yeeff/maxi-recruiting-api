package com.horizonx.recruiting_microservices.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Catálogo de niveles de estudio.
 */
@Entity
@Table(name = "niveles_estudio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelEstudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "orden")
    private Integer orden;

    @OneToMany(mappedBy = "nivelEstudio")
    @Builder.Default
    private List<Candidato> candidatos = new ArrayList<>();

    @OneToMany(mappedBy = "nivelEstudio")
    @Builder.Default
    private List<Educacion> educaciones = new ArrayList<>();
}

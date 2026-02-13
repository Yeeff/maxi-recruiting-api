package com.horizonx.recruiting_microservices.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una ciudad.
 */
@Entity
@Table(name = "ciudades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "codigo", length = 10)
    private String codigo;

    @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Barrio> barrios = new ArrayList<>();

    @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Candidato> candidatos = new ArrayList<>();
}

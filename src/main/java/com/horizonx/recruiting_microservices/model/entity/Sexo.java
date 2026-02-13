package com.horizonx.recruiting_microservices.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Catálogo de sexos.
 */
@Entity
@Table(name = "sexos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @OneToMany(mappedBy = "sexo")
    @Builder.Default
    private List<Candidato> candidatos = new ArrayList<>();
}

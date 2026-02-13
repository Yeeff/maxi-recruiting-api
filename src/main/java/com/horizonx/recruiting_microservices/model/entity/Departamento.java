package com.horizonx.recruiting_microservices.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un departamento (división geográfica).
 */
@Entity
@Table(name = "departamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "codigo", length = 10)
    private String codigo;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Ciudad> ciudades = new ArrayList<>();
}

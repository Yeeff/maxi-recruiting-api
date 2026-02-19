package com.horizonx.recruiting_microservices.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa una observación de un candidato en el proceso de reclutamiento.
 */
@Entity
@Table(name = "observaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Observacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    @JsonIgnore
    private Candidato candidato;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "tipo_observacion", length = 50)
    @Enumerated(EnumType.STRING)
    private TipoObservacion tipoObservacion;

    @Column(name = "es_interna")
    @Builder.Default
    private Boolean esInterna = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Tipos de observación disponibles.
     */
    public enum TipoObservacion {
        ENTREVISTA,
        EVALUACION,
        REFERENCIA,
        DOCUMENTACION,
        GENERAL,
        SEGUIMIENTO
    }
}

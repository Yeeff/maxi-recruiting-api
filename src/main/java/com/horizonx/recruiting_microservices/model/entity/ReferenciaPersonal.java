package com.horizonx.recruiting_microservices.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa las referencias personales de un candidato.
 */
@Entity
@Table(name = "referencias_personales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferenciaPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @Column(name = "nombre_completo", length = 100)
    private String nombreCompleto;

    @Column(name = "parentesco", length = 50)
    private String parentesco;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_referencia", length = 20)
    private TipoReferencia tipoReferencia;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TipoReferencia {
        PERSONAL,
        LABORAL,
        FAMILIAR,
        ACADEMICA
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

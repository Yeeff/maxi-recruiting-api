package com.horizonx.recruiting_microservices.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidad que representa la experiencia laboral de un candidato.
 */
@Entity
@Table(name = "experiencias_laborales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienciaLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @Column(name = "empresa", length = 100)
    private String empresa;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "descripcion_funciones", columnDefinition = "TEXT")
    private String descripcionFunciones;

    @Column(name = "nombre_jefe_inmediato", length = 100)
    private String nombreJefeInmediato;

    @Column(name = "telefono_empresa", length = 20)
    private String telefonoEmpresa;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(name = "fecha_retiro")
    private LocalDate fechaRetiro;

    @Column(name = "es_actual")
    private Boolean esActual;

    @Column(name = "motivo_retiro", length = 255)
    private String motivoRetiro;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}

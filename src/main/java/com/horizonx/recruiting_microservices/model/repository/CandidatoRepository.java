package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    Optional<Candidato> findByDocumentoIdentidad(String documentoIdentidad);

    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    List<Candidato> findByEstadoCandidato(Candidato.EstadoCandidato estadoCandidato);

    List<Candidato> findByNombre1ContainingIgnoreCaseOrApellido1ContainingIgnoreCase(
            String nombre1, String apellido1);

    @Query("SELECT c FROM Candidato c WHERE c.barrio.id = :barrioId")
    List<Candidato> findByBarrioId(@Param("barrioId") Long barrioId);

    @Query("SELECT c FROM Candidato c WHERE c.ciudad.id = :ciudadId")
    List<Candidato> findByCiudadId(@Param("ciudadId") Long ciudadId);

    @Query("SELECT c FROM Candidato c LEFT JOIN FETCH c.experienciasLaborales WHERE c.id = :id")
    Optional<Candidato> findByIdWithExperiencias(@Param("id") Long id);

    @Query("SELECT c FROM Candidato c LEFT JOIN FETCH c.educaciones WHERE c.id = :id")
    Optional<Candidato> findByIdWithEducaciones(@Param("id") Long id);
}

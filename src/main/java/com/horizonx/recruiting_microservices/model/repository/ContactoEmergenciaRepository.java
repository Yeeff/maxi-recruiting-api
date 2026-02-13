package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.ContactoEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactoEmergenciaRepository extends JpaRepository<ContactoEmergencia, Long> {

    List<ContactoEmergencia> findByCandidatoId(Long candidatoId);
}

package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.RendezVous;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RendezVous entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
}

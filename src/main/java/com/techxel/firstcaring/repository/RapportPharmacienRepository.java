package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.RapportPharmacien;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RapportPharmacien entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RapportPharmacienRepository extends JpaRepository<RapportPharmacien, Long> {
}

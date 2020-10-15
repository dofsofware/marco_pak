package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.RapportSoignant;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RapportSoignant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RapportSoignantRepository extends JpaRepository<RapportSoignant, Long> {
}

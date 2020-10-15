package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.Assure;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Assure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssureRepository extends JpaRepository<Assure, Long> {
}

package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.RapportSoignant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RapportSoignant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RapportSoignantRepository extends JpaRepository<RapportSoignant, Long> {
	@Query("select rapportSoignant from RapportSoignant rapportSoignant where rapportSoignant.codePatient =:codePatient")
	Page<RapportSoignant> getAllRapportSoignantsByCode(Pageable pageable, @Param("codePatient") String codePatient);
}

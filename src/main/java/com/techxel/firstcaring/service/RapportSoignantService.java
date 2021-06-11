package com.techxel.firstcaring.service;

import com.techxel.firstcaring.domain.RapportSoignant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RapportSoignant}.
 */
public interface RapportSoignantService {

    /**
     * Save a rapportSoignant.
     *
     * @param rapportSoignant the entity to save.
     * @return the persisted entity.
     */
    RapportSoignant save(RapportSoignant rapportSoignant);

    /**
     * Get all the rapportSoignants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RapportSoignant> findAll(Pageable pageable);


    /**
     * Get the "id" rapportSoignant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RapportSoignant> findOne(Long id);

    /**
     * Delete the "id" rapportSoignant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

	Page<RapportSoignant> getAllRapportSoignantsByCode(Pageable pageable, String codePatient);
}

package com.techxel.firstcaring.service;

import com.techxel.firstcaring.domain.PS;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link PS}.
 */
public interface PSService {

    /**
     * Save a pS.
     *
     * @param pS the entity to save.
     * @return the persisted entity.
     */
    PS save(PS pS);

    /**
     * Get all the pS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PS> findAll(Pageable pageable);

    /**
     * Get all the pS with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<PS> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" pS.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PS> findOne(Long id);

    /**
     * Delete the "id" pS.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

	Page<PS> getAllPSByCurrentUser(Pageable pageable);
}

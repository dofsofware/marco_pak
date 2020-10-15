package com.techxel.firstcaring.service;

import com.techxel.firstcaring.domain.Assure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Assure}.
 */
public interface AssureService {

    /**
     * Save a assure.
     *
     * @param assure the entity to save.
     * @return the persisted entity.
     */
    Assure save(Assure assure);

    /**
     * Get all the assures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Assure> findAll(Pageable pageable);


    /**
     * Get the "id" assure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Assure> findOne(Long id);

    /**
     * Delete the "id" assure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

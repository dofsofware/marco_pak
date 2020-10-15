package com.techxel.firstcaring.service;

import com.techxel.firstcaring.domain.Assureur;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Assureur}.
 */
public interface AssureurService {

    /**
     * Save a assureur.
     *
     * @param assureur the entity to save.
     * @return the persisted entity.
     */
    Assureur save(Assureur assureur);

    /**
     * Get all the assureurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Assureur> findAll(Pageable pageable);

    /**
     * Get all the assureurs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Assureur> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" assureur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Assureur> findOne(Long id);

    /**
     * Delete the "id" assureur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

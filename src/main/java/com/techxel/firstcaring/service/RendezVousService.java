package com.techxel.firstcaring.service;

import com.techxel.firstcaring.domain.RendezVous;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RendezVous}.
 */
public interface RendezVousService {

    /**
     * Save a rendezVous.
     *
     * @param rendezVous the entity to save.
     * @return the persisted entity.
     */
    RendezVous save(RendezVous rendezVous);

    /**
     * Get all the rendezVous.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RendezVous> findAll(Pageable pageable);


    /**
     * Get the "id" rendezVous.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RendezVous> findOne(Long id);

    /**
     * Delete the "id" rendezVous.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

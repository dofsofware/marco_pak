package com.techxel.firstcaring.service;

import com.techxel.firstcaring.domain.Facture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Facture}.
 */
public interface FactureService {

    /**
     * Save a facture.
     *
     * @param facture the entity to save.
     * @return the persisted entity.
     */
    Facture save(Facture facture);

    /**
     * Get all the factures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Facture> findAll(Pageable pageable);


    /**
     * Get the "id" facture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Facture> findOne(Long id);

    /**
     * Delete the "id" facture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

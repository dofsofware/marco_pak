package com.techxel.firstcaring.service;

import com.techxel.firstcaring.domain.Pack;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Pack}.
 */
public interface PackService {

    /**
     * Save a pack.
     *
     * @param pack the entity to save.
     * @return the persisted entity.
     */
    Pack save(Pack pack);

    /**
     * Get all the packs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pack> findAll(Pageable pageable);


    /**
     * Get the "id" pack.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pack> findOne(Long id);

    /**
     * Delete the "id" pack.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

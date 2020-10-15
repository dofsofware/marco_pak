package com.techxel.firstcaring.service;

import com.techxel.firstcaring.domain.RapportPharmacien;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RapportPharmacien}.
 */
public interface RapportPharmacienService {

    /**
     * Save a rapportPharmacien.
     *
     * @param rapportPharmacien the entity to save.
     * @return the persisted entity.
     */
    RapportPharmacien save(RapportPharmacien rapportPharmacien);

    /**
     * Get all the rapportPharmaciens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RapportPharmacien> findAll(Pageable pageable);


    /**
     * Get the "id" rapportPharmacien.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RapportPharmacien> findOne(Long id);

    /**
     * Delete the "id" rapportPharmacien.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

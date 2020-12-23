package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.Facture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Facture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    @Query("select facture from Facture facture where facture.assureur.id =:idassureur")
    Page<Facture> findAllByIdAssureur(Pageable pageable, @Param("idassureur") Long idassureur);
}

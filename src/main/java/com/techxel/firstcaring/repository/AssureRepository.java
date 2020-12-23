package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.Assure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Assure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssureRepository extends JpaRepository<Assure, Long> {
    @Query("select assure from Assure assure where assure.user.login = ?#{principal.username}")
    Page<Assure> findAllByCurrentUser(Pageable pageable);
    @Query("select assure from Assure assure where assure.assureur.id =:idassureur")
    Page<Assure> findAllByIdAssureur(Pageable pageable, @Param("idassureur") Long idAssureur);
    @Query("select assure from Assure assure where assure.codeAssure =:codeAssure")
	Page<Assure> getAllAssuresByCode(Pageable pageable, @Param("codeAssure") String codeAssure);
}


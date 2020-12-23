package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.Assureur;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Assureur entity.
 */
@Repository
public interface AssureurRepository extends JpaRepository<Assureur, Long> {

    @Query(value = "select distinct assureur from Assureur assureur left join fetch assureur.assures",
        countQuery = "select count(distinct assureur) from Assureur assureur")
    Page<Assureur> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct assureur from Assureur assureur left join fetch assureur.assures")
    List<Assureur> findAllWithEagerRelationships();

    @Query("select assureur from Assureur assureur left join fetch assureur.assures where assureur.id =:id")
    Optional<Assureur> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select assureur from Assureur assureur where assureur.user.login = ?#{principal.username}")
    Page<Assureur> findAllByCurrentUser(Pageable pageable);
}

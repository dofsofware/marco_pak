package com.techxel.firstcaring.repository;

import com.techxel.firstcaring.domain.PS;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the PS entity.
 */
@Repository
public interface PSRepository extends JpaRepository<PS, Long> {

    @Query(value = "select distinct pS from PS pS left join fetch pS.assures",
        countQuery = "select count(distinct pS) from PS pS")
    Page<PS> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct pS from PS pS left join fetch pS.assures")
    List<PS> findAllWithEagerRelationships();

    @Query("select pS from PS pS left join fetch pS.assures where pS.id =:id")
    Optional<PS> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select pS from PS pS where pS.user.login = ?#{principal.username}")
    Page<PS> findAllByCurrentUser(Pageable pageable);
}

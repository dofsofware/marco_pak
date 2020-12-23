package com.techxel.firstcaring.service.impl;

import com.techxel.firstcaring.service.AssureurService;
import com.techxel.firstcaring.domain.Assureur;
import com.techxel.firstcaring.repository.AssureurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Assureur}.
 */
@Service
@Transactional
public class AssureurServiceImpl implements AssureurService {

    private final Logger log = LoggerFactory.getLogger(AssureurServiceImpl.class);

    private final AssureurRepository assureurRepository;

    public AssureurServiceImpl(AssureurRepository assureurRepository) {
        this.assureurRepository = assureurRepository;
    }

    @Override
    public Assureur save(Assureur assureur) {
        log.debug("Request to save Assureur : {}", assureur);
        return assureurRepository.save(assureur);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Assureur> findAll(Pageable pageable) {
        log.debug("Request to get all Assureurs");
        return assureurRepository.findAll(pageable);
    }


    public Page<Assureur> findAllWithEagerRelationships(Pageable pageable) {
        return assureurRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Assureur> findOne(Long id) {
        log.debug("Request to get Assureur : {}", id);
        return assureurRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Assureur : {}", id);
        assureurRepository.deleteById(id);
    }

    @Override
    public Page<Assureur> getAllAssureursByCurrentUser(Pageable pageable) {
        return assureurRepository.findAllByCurrentUser(pageable);
    }
}

package com.techxel.firstcaring.service.impl;

import com.techxel.firstcaring.service.AssureService;
import com.techxel.firstcaring.domain.Assure;
import com.techxel.firstcaring.repository.AssureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Assure}.
 */
@Service
@Transactional
public class AssureServiceImpl implements AssureService {

    private final Logger log = LoggerFactory.getLogger(AssureServiceImpl.class);

    private final AssureRepository assureRepository;

    public AssureServiceImpl(AssureRepository assureRepository) {
        this.assureRepository = assureRepository;
    }

    @Override
    public Assure save(Assure assure) {
        log.debug("Request to save Assure : {}", assure);
        return assureRepository.save(assure);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Assure> findAll(Pageable pageable) {
        log.debug("Request to get all Assures");
        return assureRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Assure> findOne(Long id) {
        log.debug("Request to get Assure : {}", id);
        return assureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Assure : {}", id);
        assureRepository.deleteById(id);
    }

    @Override
    public Page<Assure> findAllByCurrentUser(Pageable pageable) {
        return assureRepository.findAllByCurrentUser(pageable);
    }

    @Override
    public Page<Assure> findAllByIdAssureur(Pageable pageable, Long idAssureur) {
        return assureRepository.findAllByIdAssureur(pageable,idAssureur);
    }

    @Override
    public Page<Assure> getAllAssuresByCode(Pageable pageable, String codeAssure) {
        return assureRepository.getAllAssuresByCode(pageable,codeAssure);
    }
}

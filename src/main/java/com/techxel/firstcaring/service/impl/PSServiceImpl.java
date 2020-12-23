package com.techxel.firstcaring.service.impl;

import com.techxel.firstcaring.service.PSService;
import com.techxel.firstcaring.domain.PS;
import com.techxel.firstcaring.repository.PSRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PS}.
 */
@Service
@Transactional
public class PSServiceImpl implements PSService {

    private final Logger log = LoggerFactory.getLogger(PSServiceImpl.class);

    private final PSRepository pSRepository;

    public PSServiceImpl(PSRepository pSRepository) {
        this.pSRepository = pSRepository;
    }

    @Override
    public PS save(PS pS) {
        log.debug("Request to save PS : {}", pS);
        return pSRepository.save(pS);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PS> findAll(Pageable pageable) {
        log.debug("Request to get all PS");
        return pSRepository.findAll(pageable);
    }


    public Page<PS> findAllWithEagerRelationships(Pageable pageable) {
        return pSRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PS> findOne(Long id) {
        log.debug("Request to get PS : {}", id);
        return pSRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PS : {}", id);
        pSRepository.deleteById(id);
    }

    @Override
    public Page<PS> getAllPSByCurrentUser(Pageable pageable) {
        
        return pSRepository.findAllByCurrentUser(pageable);
    }
}

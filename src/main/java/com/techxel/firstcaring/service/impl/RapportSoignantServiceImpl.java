package com.techxel.firstcaring.service.impl;

import com.techxel.firstcaring.service.RapportSoignantService;
import com.techxel.firstcaring.domain.RapportSoignant;
import com.techxel.firstcaring.repository.RapportSoignantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RapportSoignant}.
 */
@Service
@Transactional
public class RapportSoignantServiceImpl implements RapportSoignantService {

    private final Logger log = LoggerFactory.getLogger(RapportSoignantServiceImpl.class);

    private final RapportSoignantRepository rapportSoignantRepository;

    public RapportSoignantServiceImpl(RapportSoignantRepository rapportSoignantRepository) {
        this.rapportSoignantRepository = rapportSoignantRepository;
    }

    @Override
    public RapportSoignant save(RapportSoignant rapportSoignant) {
        log.debug("Request to save RapportSoignant : {}", rapportSoignant);
        return rapportSoignantRepository.save(rapportSoignant);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RapportSoignant> findAll(Pageable pageable) {
        log.debug("Request to get all RapportSoignants");
        return rapportSoignantRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RapportSoignant> findOne(Long id) {
        log.debug("Request to get RapportSoignant : {}", id);
        return rapportSoignantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RapportSoignant : {}", id);
        rapportSoignantRepository.deleteById(id);
    }

    @Override
    public Page<RapportSoignant> getAllRapportSoignantsByCode(Pageable pageable, String codePatient) {
        return rapportSoignantRepository.getAllRapportSoignantsByCode(pageable,codePatient);
    }
}

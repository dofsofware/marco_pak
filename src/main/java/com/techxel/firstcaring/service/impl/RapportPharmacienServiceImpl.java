package com.techxel.firstcaring.service.impl;

import com.techxel.firstcaring.service.RapportPharmacienService;
import com.techxel.firstcaring.domain.RapportPharmacien;
import com.techxel.firstcaring.repository.RapportPharmacienRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RapportPharmacien}.
 */
@Service
@Transactional
public class RapportPharmacienServiceImpl implements RapportPharmacienService {

    private final Logger log = LoggerFactory.getLogger(RapportPharmacienServiceImpl.class);

    private final RapportPharmacienRepository rapportPharmacienRepository;

    public RapportPharmacienServiceImpl(RapportPharmacienRepository rapportPharmacienRepository) {
        this.rapportPharmacienRepository = rapportPharmacienRepository;
    }

    @Override
    public RapportPharmacien save(RapportPharmacien rapportPharmacien) {
        log.debug("Request to save RapportPharmacien : {}", rapportPharmacien);
        return rapportPharmacienRepository.save(rapportPharmacien);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RapportPharmacien> findAll(Pageable pageable) {
        log.debug("Request to get all RapportPharmaciens");
        return rapportPharmacienRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RapportPharmacien> findOne(Long id) {
        log.debug("Request to get RapportPharmacien : {}", id);
        return rapportPharmacienRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RapportPharmacien : {}", id);
        rapportPharmacienRepository.deleteById(id);
    }
}

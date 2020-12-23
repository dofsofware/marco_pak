package com.techxel.firstcaring.service.impl;

import com.techxel.firstcaring.service.FactureService;
import com.techxel.firstcaring.domain.Facture;
import com.techxel.firstcaring.repository.FactureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Facture}.
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureServiceImpl.class);

    private final FactureRepository factureRepository;

    public FactureServiceImpl(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    @Override
    public Facture save(Facture facture) {
        log.debug("Request to save Facture : {}", facture);
        return factureRepository.save(facture);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Facture> findAll(Pageable pageable) {
        log.debug("Request to get all Factures");
        return factureRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        return factureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.deleteById(id);
    }

    @Override
    public Page<Facture> findAllByIdAssureur(Pageable pageable, Long idAssureur) {

        return factureRepository.findAllByIdAssureur(pageable,idAssureur);
    }
}

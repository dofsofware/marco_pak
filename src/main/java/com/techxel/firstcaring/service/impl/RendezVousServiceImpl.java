package com.techxel.firstcaring.service.impl;

import com.techxel.firstcaring.service.RendezVousService;
import com.techxel.firstcaring.domain.RendezVous;
import com.techxel.firstcaring.repository.RendezVousRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RendezVous}.
 */
@Service
@Transactional
public class RendezVousServiceImpl implements RendezVousService {

    private final Logger log = LoggerFactory.getLogger(RendezVousServiceImpl.class);

    private final RendezVousRepository rendezVousRepository;

    public RendezVousServiceImpl(RendezVousRepository rendezVousRepository) {
        this.rendezVousRepository = rendezVousRepository;
    }

    @Override
    public RendezVous save(RendezVous rendezVous) {
        log.debug("Request to save RendezVous : {}", rendezVous);
        return rendezVousRepository.save(rendezVous);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RendezVous> findAll(Pageable pageable) {
        log.debug("Request to get all RendezVous");
        return rendezVousRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RendezVous> findOne(Long id) {
        log.debug("Request to get RendezVous : {}", id);
        return rendezVousRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RendezVous : {}", id);
        rendezVousRepository.deleteById(id);
    }
}

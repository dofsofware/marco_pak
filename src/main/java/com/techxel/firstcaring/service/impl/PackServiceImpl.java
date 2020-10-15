package com.techxel.firstcaring.service.impl;

import com.techxel.firstcaring.service.PackService;
import com.techxel.firstcaring.domain.Pack;
import com.techxel.firstcaring.repository.PackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Pack}.
 */
@Service
@Transactional
public class PackServiceImpl implements PackService {

    private final Logger log = LoggerFactory.getLogger(PackServiceImpl.class);

    private final PackRepository packRepository;

    public PackServiceImpl(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    @Override
    public Pack save(Pack pack) {
        log.debug("Request to save Pack : {}", pack);
        return packRepository.save(pack);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pack> findAll(Pageable pageable) {
        log.debug("Request to get all Packs");
        return packRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Pack> findOne(Long id) {
        log.debug("Request to get Pack : {}", id);
        return packRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pack : {}", id);
        packRepository.deleteById(id);
    }
}

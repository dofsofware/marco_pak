package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.domain.RapportSoignant;
import com.techxel.firstcaring.service.RapportSoignantService;
import com.techxel.firstcaring.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.techxel.firstcaring.domain.RapportSoignant}.
 */
@RestController
@RequestMapping("/api")
public class RapportSoignantResource {

    private final Logger log = LoggerFactory.getLogger(RapportSoignantResource.class);

    private static final String ENTITY_NAME = "rapportSoignant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RapportSoignantService rapportSoignantService;

    public RapportSoignantResource(RapportSoignantService rapportSoignantService) {
        this.rapportSoignantService = rapportSoignantService;
    }

    /**
     * {@code POST  /rapport-soignants} : Create a new rapportSoignant.
     *
     * @param rapportSoignant the rapportSoignant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rapportSoignant, or with status {@code 400 (Bad Request)} if the rapportSoignant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rapport-soignants")
    public ResponseEntity<RapportSoignant> createRapportSoignant(@Valid @RequestBody RapportSoignant rapportSoignant) throws URISyntaxException {
        log.debug("REST request to save RapportSoignant : {}", rapportSoignant);
        if (rapportSoignant.getId() != null) {
            throw new BadRequestAlertException("A new rapportSoignant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RapportSoignant result = rapportSoignantService.save(rapportSoignant);
        return ResponseEntity.created(new URI("/api/rapport-soignants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rapport-soignants} : Updates an existing rapportSoignant.
     *
     * @param rapportSoignant the rapportSoignant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapportSoignant,
     * or with status {@code 400 (Bad Request)} if the rapportSoignant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rapportSoignant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rapport-soignants")
    public ResponseEntity<RapportSoignant> updateRapportSoignant(@Valid @RequestBody RapportSoignant rapportSoignant) throws URISyntaxException {
        log.debug("REST request to update RapportSoignant : {}", rapportSoignant);
        if (rapportSoignant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RapportSoignant result = rapportSoignantService.save(rapportSoignant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapportSoignant.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rapport-soignants} : get all the rapportSoignants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rapportSoignants in body.
     */
    @GetMapping("/rapport-soignants")
    public ResponseEntity<List<RapportSoignant>> getAllRapportSoignants(Pageable pageable) {
        log.debug("REST request to get a page of RapportSoignants");
        Page<RapportSoignant> page = rapportSoignantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    
    /**
     * {@code GET  /rapport-soignants} : get all the rapportSoignants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rapportSoignants in body.
     */
    @GetMapping("/rapport-soignants/getAllRapportSoignantsByCode/{codePatient}")
    public ResponseEntity<List<RapportSoignant>> getAllRapportSoignantsByCode(Pageable pageable, @PathVariable String codePatient) {
        log.debug("REST request to get a page of RapportSoignants");
        Page<RapportSoignant> page = rapportSoignantService.getAllRapportSoignantsByCode(pageable, codePatient);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rapport-soignants/:id} : get the "id" rapportSoignant.
     *
     * @param id the id of the rapportSoignant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rapportSoignant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rapport-soignants/{id}")
    public ResponseEntity<RapportSoignant> getRapportSoignant(@PathVariable Long id) {
        log.debug("REST request to get RapportSoignant : {}", id);
        Optional<RapportSoignant> rapportSoignant = rapportSoignantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rapportSoignant);
    }

    /**
     * {@code DELETE  /rapport-soignants/:id} : delete the "id" rapportSoignant.
     *
     * @param id the id of the rapportSoignant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rapport-soignants/{id}")
    public ResponseEntity<Void> deleteRapportSoignant(@PathVariable Long id) {
        log.debug("REST request to delete RapportSoignant : {}", id);
        rapportSoignantService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

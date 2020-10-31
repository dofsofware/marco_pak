package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.domain.RapportPharmacien;
import com.techxel.firstcaring.service.RapportPharmacienService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.techxel.firstcaring.domain.RapportPharmacien}.
 */
@RestController
@RequestMapping("/api")
public class RapportPharmacienResource {

    private final Logger log = LoggerFactory.getLogger(RapportPharmacienResource.class);

    private static final String ENTITY_NAME = "rapportPharmacien";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RapportPharmacienService rapportPharmacienService;

    public RapportPharmacienResource(RapportPharmacienService rapportPharmacienService) {
        this.rapportPharmacienService = rapportPharmacienService;
    }

    /**
     * {@code POST  /rapport-pharmaciens} : Create a new rapportPharmacien.
     *
     * @param rapportPharmacien the rapportPharmacien to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rapportPharmacien, or with status {@code 400 (Bad Request)} if the rapportPharmacien has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rapport-pharmaciens")
    public ResponseEntity<RapportPharmacien> createRapportPharmacien(@Valid @RequestBody RapportPharmacien rapportPharmacien) throws URISyntaxException {
        log.debug("REST request to save RapportPharmacien : {}", rapportPharmacien);
        if (rapportPharmacien.getId() != null) {
            throw new BadRequestAlertException("A new rapportPharmacien cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RapportPharmacien result = rapportPharmacienService.save(rapportPharmacien);
        return ResponseEntity.created(new URI("/api/rapport-pharmaciens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rapport-pharmaciens} : Updates an existing rapportPharmacien.
     *
     * @param rapportPharmacien the rapportPharmacien to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapportPharmacien,
     * or with status {@code 400 (Bad Request)} if the rapportPharmacien is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rapportPharmacien couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rapport-pharmaciens")
    public ResponseEntity<RapportPharmacien> updateRapportPharmacien(@Valid @RequestBody RapportPharmacien rapportPharmacien) throws URISyntaxException {
        log.debug("REST request to update RapportPharmacien : {}", rapportPharmacien);
        if (rapportPharmacien.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RapportPharmacien result = rapportPharmacienService.save(rapportPharmacien);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapportPharmacien.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rapport-pharmaciens} : get all the rapportPharmaciens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rapportPharmaciens in body.
     */
    @GetMapping("/rapport-pharmaciens")
    public ResponseEntity<List<RapportPharmacien>> getAllRapportPharmaciens(Pageable pageable) {
        log.debug("REST request to get a page of RapportPharmaciens");
        Page<RapportPharmacien> page = rapportPharmacienService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rapport-pharmaciens/:id} : get the "id" rapportPharmacien.
     *
     * @param id the id of the rapportPharmacien to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rapportPharmacien, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rapport-pharmaciens/{id}")
    public ResponseEntity<RapportPharmacien> getRapportPharmacien(@PathVariable Long id) {
        log.debug("REST request to get RapportPharmacien : {}", id);
        Optional<RapportPharmacien> rapportPharmacien = rapportPharmacienService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rapportPharmacien);
    }

    /**
     * {@code DELETE  /rapport-pharmaciens/:id} : delete the "id" rapportPharmacien.
     *
     * @param id the id of the rapportPharmacien to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rapport-pharmaciens/{id}")
    public ResponseEntity<Void> deleteRapportPharmacien(@PathVariable Long id) {
        log.debug("REST request to delete RapportPharmacien : {}", id);
        rapportPharmacienService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

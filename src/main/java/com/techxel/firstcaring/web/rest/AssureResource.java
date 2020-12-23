package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.domain.Assure;
import com.techxel.firstcaring.domain.User;
import com.techxel.firstcaring.domain.enumeration.Profil;
import com.techxel.firstcaring.service.AssureService;
import com.techxel.firstcaring.service.UserService;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.techxel.firstcaring.domain.Assure}.
 */
@RestController
@RequestMapping("/api")
public class AssureResource {

    private final Logger log = LoggerFactory.getLogger(AssureResource.class);

    private static final String ENTITY_NAME = "assure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssureService assureService;
    private final UserService userService;

    public AssureResource(AssureService assureService, UserService userService) {
        this.assureService = assureService;
        this.userService = userService;
    }

    /**
     * {@code POST  /assures} : Create a new assure.
     *
     * @param assure the assure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assure, or with status {@code 400 (Bad Request)} if the assure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assures")
    public ResponseEntity<Assure> createAssure(@Valid @RequestBody Assure assure) throws URISyntaxException {
        log.debug("REST request to save Assure : {}", assure);
        if (assure.getId() != null) {
            throw new BadRequestAlertException("A new assure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        // par moi
        // -------------------------------------------------------------------------------------------------------
        
        final Optional<User> isUser = userService.getUserWithAuthorities();
        final User user = isUser.get();
        assure.setUser(user);
        assure.setProfil(Profil.ASSURE);
        assure.setCreatedAt(ZonedDateTime.now());
        // par moi -----------------------------------------------------------------------------------------------
        Assure result = assureService.save(assure);
        
        return ResponseEntity.created(new URI("/api/assures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assures} : Updates an existing assure.
     *
     * @param assure the assure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assure,
     * or with status {@code 400 (Bad Request)} if the assure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assures")
    public ResponseEntity<Assure> updateAssure(@Valid @RequestBody Assure assure) throws URISyntaxException {
        log.debug("REST request to update Assure : {}", assure);
        if (assure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Assure result = assureService.save(assure);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assure.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /assures} : get all the assures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assures in body.
     */
    @GetMapping("/assures")
    public ResponseEntity<List<Assure>> getAllAssures(Pageable pageable) {
        log.debug("REST request to get a page of Assures");
        Page<Assure> page = assureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * {@code GET  /assures} : get all the assures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assures in body.
     */
    @GetMapping("/assures/getAllAssuresByCurrentUser")
    public ResponseEntity<List<Assure>> getAllAssuresByCurrentUser(Pageable pageable) {
        log.debug("REST request to get a page of Assures");
        Page<Assure> page = assureService.findAllByCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    
    
    /**
     * {@code GET  /assures} : get all the assures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assures in body.
     */
    @GetMapping("/assures/findAllByIdAssureur/{IdAssureur}")
    public ResponseEntity<List<Assure>> getAllAssuresByIdAssureur(Pageable pageable, @PathVariable Long IdAssureur) {
        log.debug("REST request to get a page of Assures");
        Page<Assure> page = assureService.findAllByIdAssureur(pageable, IdAssureur);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    
    /**
     * {@code GET  /assures} : get all the assures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assures in body.
     */
    @GetMapping("/assures/getAllAssuresByCode/{codeAssure}")
    public ResponseEntity<List<Assure>> getAllAssuresByCode(Pageable pageable, @PathVariable String codeAssure) {
        log.debug("REST request to get a page of Assures");
        Page<Assure> page = assureService.getAllAssuresByCode(pageable, codeAssure);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assures/:id} : get the "id" assure.
     *
     * @param id the id of the assure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assures/{id}")
    public ResponseEntity<Assure> getAssure(@PathVariable Long id) {
        log.debug("REST request to get Assure : {}", id);
        Optional<Assure> assure = assureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assure);
    }

    /**
     * {@code DELETE  /assures/:id} : delete the "id" assure.
     *
     * @param id the id of the assure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assures/{id}")
    public ResponseEntity<Void> deleteAssure(@PathVariable Long id) {
        log.debug("REST request to delete Assure : {}", id);
        assureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

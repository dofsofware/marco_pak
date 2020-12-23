package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.domain.PS;
import com.techxel.firstcaring.domain.User;
import com.techxel.firstcaring.domain.enumeration.Profil;
import com.techxel.firstcaring.service.PSService;
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
 * REST controller for managing {@link com.techxel.firstcaring.domain.PS}.
 */
@RestController
@RequestMapping("/api")
public class PSResource {

    private final Logger log = LoggerFactory.getLogger(PSResource.class);

    private static final String ENTITY_NAME = "pS";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PSService pSService;
    private final UserService userService;

    public PSResource(PSService pSService, UserService userService) {
        this.pSService = pSService;
        this.userService = userService; 
    }

    /**
     * {@code POST  /ps} : Create a new pS.
     *
     * @param pS the pS to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pS, or with status {@code 400 (Bad Request)} if the pS has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ps")
    public ResponseEntity<PS> createPS(@Valid @RequestBody PS pS) throws URISyntaxException {
        log.debug("REST request to save PS : {}", pS);
        if (pS.getId() != null) {
            throw new BadRequestAlertException("A new pS cannot already have an ID", ENTITY_NAME, "idexists");
        }
        // par moi
        // -------------------------------------------------------------------------------------------------------
        
        final Optional<User> isUser = userService.getUserWithAuthorities();
        final User user = isUser.get();
        pS.setUser(user);
        pS.setProfil(Profil.PS);
        pS.setCreatedAt(ZonedDateTime.now());
        // par moi -----------------------------------------------------------------------------------------------
        PS result = pSService.save(pS);
        return ResponseEntity.created(new URI("/api/ps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ps} : Updates an existing pS.
     *
     * @param pS the pS to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pS,
     * or with status {@code 400 (Bad Request)} if the pS is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pS couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ps")
    public ResponseEntity<PS> updatePS(@Valid @RequestBody PS pS) throws URISyntaxException {
        log.debug("REST request to update PS : {}", pS);
        if (pS.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PS result = pSService.save(pS);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pS.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ps} : get all the pS.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pS in body.
     */
    @GetMapping("/ps")
    public ResponseEntity<List<PS>> getAllPS(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of PS");
        Page<PS> page;
        if (eagerload) {
            page = pSService.findAllWithEagerRelationships(pageable);
        } else {
            page = pSService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ps} : get all the pS.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pS in body.
     */
    @GetMapping("/ps/getAllPSByCurrentUser")
    public ResponseEntity<List<PS>> getAllPSByCurrentUser(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of PS");
        Page<PS> page;
        if (eagerload) {
            page = pSService.findAllWithEagerRelationships(pageable);
        } else {
            page = pSService.getAllPSByCurrentUser(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ps/:id} : get the "id" pS.
     *
     * @param id the id of the pS to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pS, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ps/{id}")
    public ResponseEntity<PS> getPS(@PathVariable Long id) {
        log.debug("REST request to get PS : {}", id);
        Optional<PS> pS = pSService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pS);
    }

    /**
     * {@code DELETE  /ps/:id} : delete the "id" pS.
     *
     * @param id the id of the pS to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ps/{id}")
    public ResponseEntity<Void> deletePS(@PathVariable Long id) {
        log.debug("REST request to delete PS : {}", id);
        pSService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

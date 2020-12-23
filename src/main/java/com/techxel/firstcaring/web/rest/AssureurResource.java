package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.domain.Assureur;
import com.techxel.firstcaring.domain.User;
import com.techxel.firstcaring.domain.enumeration.Profil;
import com.techxel.firstcaring.service.AssureurService;
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
 * REST controller for managing {@link com.techxel.firstcaring.domain.Assureur}.
 */
@RestController
@RequestMapping("/api")
public class AssureurResource {

    private final Logger log = LoggerFactory.getLogger(AssureurResource.class);

    private static final String ENTITY_NAME = "assureur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssureurService assureurService;
    private final UserService userService;

    public AssureurResource(AssureurService assureurService, UserService userService) {
        this.assureurService = assureurService;
        this.userService = userService;
    }

    /**
     * {@code POST  /assureurs} : Create a new assureur.
     *
     * @param assureur the assureur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assureur, or with status {@code 400 (Bad Request)} if the assureur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assureurs")
    public ResponseEntity<Assureur> createAssureur(@Valid @RequestBody Assureur assureur) throws URISyntaxException {
        log.debug("REST request to save Assureur : {}", assureur);
        if (assureur.getId() != null) {
            throw new BadRequestAlertException("A new assureur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        // par moi
        // -------------------------------------------------------------------------------------------------------
        
        final Optional<User> isUser = userService.getUserWithAuthorities();
        final User user = isUser.get();
        assureur.setUser(user);
        assureur.setProfil(Profil.ASSUREUR);
        assureur.setCreatedAt(ZonedDateTime.now());
        // par moi -------------------------------------------------------------------------------------------------------
        Assureur result = assureurService.save(assureur);
        return ResponseEntity.created(new URI("/api/assureurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assureurs} : Updates an existing assureur.
     *
     * @param assureur the assureur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assureur,
     * or with status {@code 400 (Bad Request)} if the assureur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assureur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assureurs")
    public ResponseEntity<Assureur> updateAssureur(@Valid @RequestBody Assureur assureur) throws URISyntaxException {
        log.debug("REST request to update Assureur : {}", assureur);
        if (assureur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Assureur result = assureurService.save(assureur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assureur.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /assureurs} : get all the assureurs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assureurs in body.
     */
    @GetMapping("/assureurs")
    public ResponseEntity<List<Assureur>> getAllAssureurs(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Assureurs");
        Page<Assureur> page;
        if (eagerload) {
            page = assureurService.findAllWithEagerRelationships(pageable);
        } else {
            page = assureurService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assureurs} : get all the assureurs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assureurs in body.
     */
    @GetMapping("/assureurs/getAllAssureursByCurrentUser")
    public ResponseEntity<List<Assureur>> getAllAssureursByCurrentUser(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Assureurs");
        Page<Assureur> page;
        if (eagerload) {
            page = assureurService.findAllWithEagerRelationships(pageable);
        } else {
            page = assureurService.getAllAssureursByCurrentUser(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assureurs/:id} : get the "id" assureur.
     *
     * @param id the id of the assureur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assureur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assureurs/{id}")
    public ResponseEntity<Assureur> getAssureur(@PathVariable Long id) {
        log.debug("REST request to get Assureur : {}", id);
        Optional<Assureur> assureur = assureurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assureur);
    }

    /**
     * {@code DELETE  /assureurs/:id} : delete the "id" assureur.
     *
     * @param id the id of the assureur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assureurs/{id}")
    public ResponseEntity<Void> deleteAssureur(@PathVariable Long id) {
        log.debug("REST request to delete Assureur : {}", id);
        assureurService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

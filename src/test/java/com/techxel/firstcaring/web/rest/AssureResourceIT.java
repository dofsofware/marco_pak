package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.FirstCaringApp;
import com.techxel.firstcaring.domain.Assure;
import com.techxel.firstcaring.repository.AssureRepository;
import com.techxel.firstcaring.service.AssureService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.techxel.firstcaring.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.firstcaring.domain.enumeration.Profil;
import com.techxel.firstcaring.domain.enumeration.Sexe;
/**
 * Integration tests for the {@link AssureResource} REST controller.
 */
@SpringBootTest(classes = FirstCaringApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AssureResourceIT {

    private static final String DEFAULT_CODE_ASSURE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ASSURE = "BBBBBBBBBB";

    private static final Profil DEFAULT_PROFIL = Profil.ASSUREUR;
    private static final Profil UPDATED_PROFIL = Profil.ASSURE;

    private static final Sexe DEFAULT_SEXE = Sexe.MASCULIN;
    private static final Sexe UPDATED_SEXE = Sexe.FEMININ;

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_URL_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    @Autowired
    private AssureRepository assureRepository;

    @Autowired
    private AssureService assureService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssureMockMvc;

    private Assure assure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assure createEntity(EntityManager em) {
        Assure assure = new Assure()
            .codeAssure(DEFAULT_CODE_ASSURE)
            .profil(DEFAULT_PROFIL)
            .sexe(DEFAULT_SEXE)
            .telephone(DEFAULT_TELEPHONE)
            .createdAt(DEFAULT_CREATED_AT)
            .urlPhoto(DEFAULT_URL_PHOTO)
            .adresse(DEFAULT_ADRESSE);
        return assure;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assure createUpdatedEntity(EntityManager em) {
        Assure assure = new Assure()
            .codeAssure(UPDATED_CODE_ASSURE)
            .profil(UPDATED_PROFIL)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .urlPhoto(UPDATED_URL_PHOTO)
            .adresse(UPDATED_ADRESSE);
        return assure;
    }

    @BeforeEach
    public void initTest() {
        assure = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssure() throws Exception {
        int databaseSizeBeforeCreate = assureRepository.findAll().size();
        // Create the Assure
        restAssureMockMvc.perform(post("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isCreated());

        // Validate the Assure in the database
        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeCreate + 1);
        Assure testAssure = assureList.get(assureList.size() - 1);
        assertThat(testAssure.getCodeAssure()).isEqualTo(DEFAULT_CODE_ASSURE);
        // assertThat(testAssure.getProfil()).isEqualTo(DEFAULT_PROFIL);
        assertThat(testAssure.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testAssure.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        // assertThat(testAssure.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAssure.getUrlPhoto()).isEqualTo(DEFAULT_URL_PHOTO);
        assertThat(testAssure.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    public void createAssureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assureRepository.findAll().size();

        // Create the Assure with an existing ID
        assure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssureMockMvc.perform(post("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isBadRequest());

        // Validate the Assure in the database
        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeAssureIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureRepository.findAll().size();
        // set the field null
        assure.setCodeAssure(null);

        // Create the Assure, which fails.


        restAssureMockMvc.perform(post("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isBadRequest());

        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProfilIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureRepository.findAll().size();
        // set the field null
        assure.setProfil(null);

        // Create the Assure, which fails.


        restAssureMockMvc.perform(post("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isBadRequest());

        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureRepository.findAll().size();
        // set the field null
        assure.setSexe(null);

        // Create the Assure, which fails.


        restAssureMockMvc.perform(post("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isBadRequest());

        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureRepository.findAll().size();
        // set the field null
        assure.setTelephone(null);

        // Create the Assure, which fails.


        restAssureMockMvc.perform(post("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isBadRequest());

        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureRepository.findAll().size();
        // set the field null
        assure.setCreatedAt(null);

        // Create the Assure, which fails.


        restAssureMockMvc.perform(post("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isBadRequest());

        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureRepository.findAll().size();
        // set the field null
        assure.setAdresse(null);

        // Create the Assure, which fails.


        restAssureMockMvc.perform(post("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isBadRequest());

        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssures() throws Exception {
        // Initialize the database
        assureRepository.saveAndFlush(assure);

        // Get all the assureList
        restAssureMockMvc.perform(get("/api/assures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assure.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssure").value(hasItem(DEFAULT_CODE_ASSURE)))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }
    
    @Test
    @Transactional
    public void getAssure() throws Exception {
        // Initialize the database
        assureRepository.saveAndFlush(assure);

        // Get the assure
        restAssureMockMvc.perform(get("/api/assures/{id}", assure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assure.getId().intValue()))
            .andExpect(jsonPath("$.codeAssure").value(DEFAULT_CODE_ASSURE))
            .andExpect(jsonPath("$.profil").value(DEFAULT_PROFIL.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.urlPhoto").value(DEFAULT_URL_PHOTO))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE));
    }
    @Test
    @Transactional
    public void getNonExistingAssure() throws Exception {
        // Get the assure
        restAssureMockMvc.perform(get("/api/assures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssure() throws Exception {
        // Initialize the database
        assureService.save(assure);

        int databaseSizeBeforeUpdate = assureRepository.findAll().size();

        // Update the assure
        Assure updatedAssure = assureRepository.findById(assure.getId()).get();
        // Disconnect from session so that the updates on updatedAssure are not directly saved in db
        em.detach(updatedAssure);
        updatedAssure
            .codeAssure(UPDATED_CODE_ASSURE)
            .profil(UPDATED_PROFIL)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .urlPhoto(UPDATED_URL_PHOTO)
            .adresse(UPDATED_ADRESSE);

        restAssureMockMvc.perform(put("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAssure)))
            .andExpect(status().isOk());

        // Validate the Assure in the database
        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeUpdate);
        Assure testAssure = assureList.get(assureList.size() - 1);
        assertThat(testAssure.getCodeAssure()).isEqualTo(UPDATED_CODE_ASSURE);
        assertThat(testAssure.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testAssure.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testAssure.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAssure.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAssure.getUrlPhoto()).isEqualTo(UPDATED_URL_PHOTO);
        assertThat(testAssure.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void updateNonExistingAssure() throws Exception {
        int databaseSizeBeforeUpdate = assureRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssureMockMvc.perform(put("/api/assures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assure)))
            .andExpect(status().isBadRequest());

        // Validate the Assure in the database
        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAssure() throws Exception {
        // Initialize the database
        assureService.save(assure);

        int databaseSizeBeforeDelete = assureRepository.findAll().size();

        // Delete the assure
        restAssureMockMvc.perform(delete("/api/assures/{id}", assure.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assure> assureList = assureRepository.findAll();
        assertThat(assureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

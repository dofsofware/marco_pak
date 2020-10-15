package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.FirstCaringApp;
import com.techxel.firstcaring.domain.RendezVous;
import com.techxel.firstcaring.repository.RendezVousRepository;
import com.techxel.firstcaring.service.RendezVousService;

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

/**
 * Integration tests for the {@link RendezVousResource} REST controller.
 */
@SpringBootTest(classes = FirstCaringApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RendezVousResourceIT {

    private static final String DEFAULT_CODE_PATIENT = "AAAAAAAAAA";
    private static final String UPDATED_CODE_PATIENT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_PS = "AAAAAAAAAA";
    private static final String UPDATED_CODE_PS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DEBUT_RV = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEBUT_RV = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FIN_RV = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIN_RV = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRendezVousMockMvc;

    private RendezVous rendezVous;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RendezVous createEntity(EntityManager em) {
        RendezVous rendezVous = new RendezVous()
            .codePatient(DEFAULT_CODE_PATIENT)
            .codePS(DEFAULT_CODE_PS)
            .debutRV(DEFAULT_DEBUT_RV)
            .finRV(DEFAULT_FIN_RV)
            .createdAt(DEFAULT_CREATED_AT);
        return rendezVous;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RendezVous createUpdatedEntity(EntityManager em) {
        RendezVous rendezVous = new RendezVous()
            .codePatient(UPDATED_CODE_PATIENT)
            .codePS(UPDATED_CODE_PS)
            .debutRV(UPDATED_DEBUT_RV)
            .finRV(UPDATED_FIN_RV)
            .createdAt(UPDATED_CREATED_AT);
        return rendezVous;
    }

    @BeforeEach
    public void initTest() {
        rendezVous = createEntity(em);
    }

    @Test
    @Transactional
    public void createRendezVous() throws Exception {
        int databaseSizeBeforeCreate = rendezVousRepository.findAll().size();
        // Create the RendezVous
        restRendezVousMockMvc.perform(post("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isCreated());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeCreate + 1);
        RendezVous testRendezVous = rendezVousList.get(rendezVousList.size() - 1);
        assertThat(testRendezVous.getCodePatient()).isEqualTo(DEFAULT_CODE_PATIENT);
        assertThat(testRendezVous.getCodePS()).isEqualTo(DEFAULT_CODE_PS);
        assertThat(testRendezVous.getDebutRV()).isEqualTo(DEFAULT_DEBUT_RV);
        assertThat(testRendezVous.getFinRV()).isEqualTo(DEFAULT_FIN_RV);
        assertThat(testRendezVous.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createRendezVousWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rendezVousRepository.findAll().size();

        // Create the RendezVous with an existing ID
        rendezVous.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRendezVousMockMvc.perform(post("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodePatientIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setCodePatient(null);

        // Create the RendezVous, which fails.


        restRendezVousMockMvc.perform(post("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodePSIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setCodePS(null);

        // Create the RendezVous, which fails.


        restRendezVousMockMvc.perform(post("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDebutRVIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setDebutRV(null);

        // Create the RendezVous, which fails.


        restRendezVousMockMvc.perform(post("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinRVIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setFinRV(null);

        // Create the RendezVous, which fails.


        restRendezVousMockMvc.perform(post("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = rendezVousRepository.findAll().size();
        // set the field null
        rendezVous.setCreatedAt(null);

        // Create the RendezVous, which fails.


        restRendezVousMockMvc.perform(post("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRendezVous() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get all the rendezVousList
        restRendezVousMockMvc.perform(get("/api/rendez-vous?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rendezVous.getId().intValue())))
            .andExpect(jsonPath("$.[*].codePatient").value(hasItem(DEFAULT_CODE_PATIENT)))
            .andExpect(jsonPath("$.[*].codePS").value(hasItem(DEFAULT_CODE_PS)))
            .andExpect(jsonPath("$.[*].debutRV").value(hasItem(sameInstant(DEFAULT_DEBUT_RV))))
            .andExpect(jsonPath("$.[*].finRV").value(hasItem(sameInstant(DEFAULT_FIN_RV))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }
    
    @Test
    @Transactional
    public void getRendezVous() throws Exception {
        // Initialize the database
        rendezVousRepository.saveAndFlush(rendezVous);

        // Get the rendezVous
        restRendezVousMockMvc.perform(get("/api/rendez-vous/{id}", rendezVous.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rendezVous.getId().intValue()))
            .andExpect(jsonPath("$.codePatient").value(DEFAULT_CODE_PATIENT))
            .andExpect(jsonPath("$.codePS").value(DEFAULT_CODE_PS))
            .andExpect(jsonPath("$.debutRV").value(sameInstant(DEFAULT_DEBUT_RV)))
            .andExpect(jsonPath("$.finRV").value(sameInstant(DEFAULT_FIN_RV)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }
    @Test
    @Transactional
    public void getNonExistingRendezVous() throws Exception {
        // Get the rendezVous
        restRendezVousMockMvc.perform(get("/api/rendez-vous/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRendezVous() throws Exception {
        // Initialize the database
        rendezVousService.save(rendezVous);

        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();

        // Update the rendezVous
        RendezVous updatedRendezVous = rendezVousRepository.findById(rendezVous.getId()).get();
        // Disconnect from session so that the updates on updatedRendezVous are not directly saved in db
        em.detach(updatedRendezVous);
        updatedRendezVous
            .codePatient(UPDATED_CODE_PATIENT)
            .codePS(UPDATED_CODE_PS)
            .debutRV(UPDATED_DEBUT_RV)
            .finRV(UPDATED_FIN_RV)
            .createdAt(UPDATED_CREATED_AT);

        restRendezVousMockMvc.perform(put("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRendezVous)))
            .andExpect(status().isOk());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
        RendezVous testRendezVous = rendezVousList.get(rendezVousList.size() - 1);
        assertThat(testRendezVous.getCodePatient()).isEqualTo(UPDATED_CODE_PATIENT);
        assertThat(testRendezVous.getCodePS()).isEqualTo(UPDATED_CODE_PS);
        assertThat(testRendezVous.getDebutRV()).isEqualTo(UPDATED_DEBUT_RV);
        assertThat(testRendezVous.getFinRV()).isEqualTo(UPDATED_FIN_RV);
        assertThat(testRendezVous.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingRendezVous() throws Exception {
        int databaseSizeBeforeUpdate = rendezVousRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRendezVousMockMvc.perform(put("/api/rendez-vous")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rendezVous)))
            .andExpect(status().isBadRequest());

        // Validate the RendezVous in the database
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRendezVous() throws Exception {
        // Initialize the database
        rendezVousService.save(rendezVous);

        int databaseSizeBeforeDelete = rendezVousRepository.findAll().size();

        // Delete the rendezVous
        restRendezVousMockMvc.perform(delete("/api/rendez-vous/{id}", rendezVous.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        assertThat(rendezVousList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

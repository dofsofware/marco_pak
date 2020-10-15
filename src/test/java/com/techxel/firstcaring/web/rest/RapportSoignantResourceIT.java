package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.FirstCaringApp;
import com.techxel.firstcaring.domain.RapportSoignant;
import com.techxel.firstcaring.repository.RapportSoignantRepository;
import com.techxel.firstcaring.service.RapportSoignantService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link RapportSoignantResource} REST controller.
 */
@SpringBootTest(classes = FirstCaringApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RapportSoignantResourceIT {

    private static final String DEFAULT_CODE_PATIENT = "AAAAAAAAAA";
    private static final String UPDATED_CODE_PATIENT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_PS = "AAAAAAAAAA";
    private static final String UPDATED_CODE_PS = "BBBBBBBBBB";

    private static final Double DEFAULT_FACTURATION = 1D;
    private static final Double UPDATED_FACTURATION = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private RapportSoignantRepository rapportSoignantRepository;

    @Autowired
    private RapportSoignantService rapportSoignantService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapportSoignantMockMvc;

    private RapportSoignant rapportSoignant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RapportSoignant createEntity(EntityManager em) {
        RapportSoignant rapportSoignant = new RapportSoignant()
            .codePatient(DEFAULT_CODE_PATIENT)
            .codePS(DEFAULT_CODE_PS)
            .facturation(DEFAULT_FACTURATION)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT);
        return rapportSoignant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RapportSoignant createUpdatedEntity(EntityManager em) {
        RapportSoignant rapportSoignant = new RapportSoignant()
            .codePatient(UPDATED_CODE_PATIENT)
            .codePS(UPDATED_CODE_PS)
            .facturation(UPDATED_FACTURATION)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);
        return rapportSoignant;
    }

    @BeforeEach
    public void initTest() {
        rapportSoignant = createEntity(em);
    }

    @Test
    @Transactional
    public void createRapportSoignant() throws Exception {
        int databaseSizeBeforeCreate = rapportSoignantRepository.findAll().size();
        // Create the RapportSoignant
        restRapportSoignantMockMvc.perform(post("/api/rapport-soignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportSoignant)))
            .andExpect(status().isCreated());

        // Validate the RapportSoignant in the database
        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeCreate + 1);
        RapportSoignant testRapportSoignant = rapportSoignantList.get(rapportSoignantList.size() - 1);
        assertThat(testRapportSoignant.getCodePatient()).isEqualTo(DEFAULT_CODE_PATIENT);
        assertThat(testRapportSoignant.getCodePS()).isEqualTo(DEFAULT_CODE_PS);
        assertThat(testRapportSoignant.getFacturation()).isEqualTo(DEFAULT_FACTURATION);
        assertThat(testRapportSoignant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRapportSoignant.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createRapportSoignantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rapportSoignantRepository.findAll().size();

        // Create the RapportSoignant with an existing ID
        rapportSoignant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapportSoignantMockMvc.perform(post("/api/rapport-soignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportSoignant)))
            .andExpect(status().isBadRequest());

        // Validate the RapportSoignant in the database
        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodePatientIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportSoignantRepository.findAll().size();
        // set the field null
        rapportSoignant.setCodePatient(null);

        // Create the RapportSoignant, which fails.


        restRapportSoignantMockMvc.perform(post("/api/rapport-soignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportSoignant)))
            .andExpect(status().isBadRequest());

        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodePSIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportSoignantRepository.findAll().size();
        // set the field null
        rapportSoignant.setCodePS(null);

        // Create the RapportSoignant, which fails.


        restRapportSoignantMockMvc.perform(post("/api/rapport-soignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportSoignant)))
            .andExpect(status().isBadRequest());

        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFacturationIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportSoignantRepository.findAll().size();
        // set the field null
        rapportSoignant.setFacturation(null);

        // Create the RapportSoignant, which fails.


        restRapportSoignantMockMvc.perform(post("/api/rapport-soignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportSoignant)))
            .andExpect(status().isBadRequest());

        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportSoignantRepository.findAll().size();
        // set the field null
        rapportSoignant.setCreatedAt(null);

        // Create the RapportSoignant, which fails.


        restRapportSoignantMockMvc.perform(post("/api/rapport-soignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportSoignant)))
            .andExpect(status().isBadRequest());

        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRapportSoignants() throws Exception {
        // Initialize the database
        rapportSoignantRepository.saveAndFlush(rapportSoignant);

        // Get all the rapportSoignantList
        restRapportSoignantMockMvc.perform(get("/api/rapport-soignants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapportSoignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].codePatient").value(hasItem(DEFAULT_CODE_PATIENT)))
            .andExpect(jsonPath("$.[*].codePS").value(hasItem(DEFAULT_CODE_PS)))
            .andExpect(jsonPath("$.[*].facturation").value(hasItem(DEFAULT_FACTURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }
    
    @Test
    @Transactional
    public void getRapportSoignant() throws Exception {
        // Initialize the database
        rapportSoignantRepository.saveAndFlush(rapportSoignant);

        // Get the rapportSoignant
        restRapportSoignantMockMvc.perform(get("/api/rapport-soignants/{id}", rapportSoignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rapportSoignant.getId().intValue()))
            .andExpect(jsonPath("$.codePatient").value(DEFAULT_CODE_PATIENT))
            .andExpect(jsonPath("$.codePS").value(DEFAULT_CODE_PS))
            .andExpect(jsonPath("$.facturation").value(DEFAULT_FACTURATION.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }
    @Test
    @Transactional
    public void getNonExistingRapportSoignant() throws Exception {
        // Get the rapportSoignant
        restRapportSoignantMockMvc.perform(get("/api/rapport-soignants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRapportSoignant() throws Exception {
        // Initialize the database
        rapportSoignantService.save(rapportSoignant);

        int databaseSizeBeforeUpdate = rapportSoignantRepository.findAll().size();

        // Update the rapportSoignant
        RapportSoignant updatedRapportSoignant = rapportSoignantRepository.findById(rapportSoignant.getId()).get();
        // Disconnect from session so that the updates on updatedRapportSoignant are not directly saved in db
        em.detach(updatedRapportSoignant);
        updatedRapportSoignant
            .codePatient(UPDATED_CODE_PATIENT)
            .codePS(UPDATED_CODE_PS)
            .facturation(UPDATED_FACTURATION)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);

        restRapportSoignantMockMvc.perform(put("/api/rapport-soignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRapportSoignant)))
            .andExpect(status().isOk());

        // Validate the RapportSoignant in the database
        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeUpdate);
        RapportSoignant testRapportSoignant = rapportSoignantList.get(rapportSoignantList.size() - 1);
        assertThat(testRapportSoignant.getCodePatient()).isEqualTo(UPDATED_CODE_PATIENT);
        assertThat(testRapportSoignant.getCodePS()).isEqualTo(UPDATED_CODE_PS);
        assertThat(testRapportSoignant.getFacturation()).isEqualTo(UPDATED_FACTURATION);
        assertThat(testRapportSoignant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRapportSoignant.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingRapportSoignant() throws Exception {
        int databaseSizeBeforeUpdate = rapportSoignantRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportSoignantMockMvc.perform(put("/api/rapport-soignants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportSoignant)))
            .andExpect(status().isBadRequest());

        // Validate the RapportSoignant in the database
        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRapportSoignant() throws Exception {
        // Initialize the database
        rapportSoignantService.save(rapportSoignant);

        int databaseSizeBeforeDelete = rapportSoignantRepository.findAll().size();

        // Delete the rapportSoignant
        restRapportSoignantMockMvc.perform(delete("/api/rapport-soignants/{id}", rapportSoignant.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RapportSoignant> rapportSoignantList = rapportSoignantRepository.findAll();
        assertThat(rapportSoignantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

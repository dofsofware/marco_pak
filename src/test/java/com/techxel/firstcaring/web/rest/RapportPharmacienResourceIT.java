package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.FirstCaringApp;
import com.techxel.firstcaring.domain.RapportPharmacien;
import com.techxel.firstcaring.repository.RapportPharmacienRepository;
import com.techxel.firstcaring.service.RapportPharmacienService;

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
 * Integration tests for the {@link RapportPharmacienResource} REST controller.
 */
@SpringBootTest(classes = FirstCaringApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RapportPharmacienResourceIT {

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
    private RapportPharmacienRepository rapportPharmacienRepository;

    @Autowired
    private RapportPharmacienService rapportPharmacienService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapportPharmacienMockMvc;

    private RapportPharmacien rapportPharmacien;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RapportPharmacien createEntity(EntityManager em) {
        RapportPharmacien rapportPharmacien = new RapportPharmacien()
            .codePatient(DEFAULT_CODE_PATIENT)
            .codePS(DEFAULT_CODE_PS)
            .facturation(DEFAULT_FACTURATION)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT);
        return rapportPharmacien;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RapportPharmacien createUpdatedEntity(EntityManager em) {
        RapportPharmacien rapportPharmacien = new RapportPharmacien()
            .codePatient(UPDATED_CODE_PATIENT)
            .codePS(UPDATED_CODE_PS)
            .facturation(UPDATED_FACTURATION)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);
        return rapportPharmacien;
    }

    @BeforeEach
    public void initTest() {
        rapportPharmacien = createEntity(em);
    }

    @Test
    @Transactional
    public void createRapportPharmacien() throws Exception {
        int databaseSizeBeforeCreate = rapportPharmacienRepository.findAll().size();
        // Create the RapportPharmacien
        restRapportPharmacienMockMvc.perform(post("/api/rapport-pharmaciens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportPharmacien)))
            .andExpect(status().isCreated());

        // Validate the RapportPharmacien in the database
        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeCreate + 1);
        RapportPharmacien testRapportPharmacien = rapportPharmacienList.get(rapportPharmacienList.size() - 1);
        assertThat(testRapportPharmacien.getCodePatient()).isEqualTo(DEFAULT_CODE_PATIENT);
        assertThat(testRapportPharmacien.getCodePS()).isEqualTo(DEFAULT_CODE_PS);
        assertThat(testRapportPharmacien.getFacturation()).isEqualTo(DEFAULT_FACTURATION);
        assertThat(testRapportPharmacien.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRapportPharmacien.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createRapportPharmacienWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rapportPharmacienRepository.findAll().size();

        // Create the RapportPharmacien with an existing ID
        rapportPharmacien.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapportPharmacienMockMvc.perform(post("/api/rapport-pharmaciens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportPharmacien)))
            .andExpect(status().isBadRequest());

        // Validate the RapportPharmacien in the database
        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodePatientIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportPharmacienRepository.findAll().size();
        // set the field null
        rapportPharmacien.setCodePatient(null);

        // Create the RapportPharmacien, which fails.


        restRapportPharmacienMockMvc.perform(post("/api/rapport-pharmaciens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportPharmacien)))
            .andExpect(status().isBadRequest());

        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodePSIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportPharmacienRepository.findAll().size();
        // set the field null
        rapportPharmacien.setCodePS(null);

        // Create the RapportPharmacien, which fails.


        restRapportPharmacienMockMvc.perform(post("/api/rapport-pharmaciens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportPharmacien)))
            .andExpect(status().isBadRequest());

        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFacturationIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportPharmacienRepository.findAll().size();
        // set the field null
        rapportPharmacien.setFacturation(null);

        // Create the RapportPharmacien, which fails.


        restRapportPharmacienMockMvc.perform(post("/api/rapport-pharmaciens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportPharmacien)))
            .andExpect(status().isBadRequest());

        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = rapportPharmacienRepository.findAll().size();
        // set the field null
        rapportPharmacien.setCreatedAt(null);

        // Create the RapportPharmacien, which fails.


        restRapportPharmacienMockMvc.perform(post("/api/rapport-pharmaciens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportPharmacien)))
            .andExpect(status().isBadRequest());

        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRapportPharmaciens() throws Exception {
        // Initialize the database
        rapportPharmacienRepository.saveAndFlush(rapportPharmacien);

        // Get all the rapportPharmacienList
        restRapportPharmacienMockMvc.perform(get("/api/rapport-pharmaciens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapportPharmacien.getId().intValue())))
            .andExpect(jsonPath("$.[*].codePatient").value(hasItem(DEFAULT_CODE_PATIENT)))
            .andExpect(jsonPath("$.[*].codePS").value(hasItem(DEFAULT_CODE_PS)))
            .andExpect(jsonPath("$.[*].facturation").value(hasItem(DEFAULT_FACTURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }
    
    @Test
    @Transactional
    public void getRapportPharmacien() throws Exception {
        // Initialize the database
        rapportPharmacienRepository.saveAndFlush(rapportPharmacien);

        // Get the rapportPharmacien
        restRapportPharmacienMockMvc.perform(get("/api/rapport-pharmaciens/{id}", rapportPharmacien.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rapportPharmacien.getId().intValue()))
            .andExpect(jsonPath("$.codePatient").value(DEFAULT_CODE_PATIENT))
            .andExpect(jsonPath("$.codePS").value(DEFAULT_CODE_PS))
            .andExpect(jsonPath("$.facturation").value(DEFAULT_FACTURATION.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }
    @Test
    @Transactional
    public void getNonExistingRapportPharmacien() throws Exception {
        // Get the rapportPharmacien
        restRapportPharmacienMockMvc.perform(get("/api/rapport-pharmaciens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRapportPharmacien() throws Exception {
        // Initialize the database
        rapportPharmacienService.save(rapportPharmacien);

        int databaseSizeBeforeUpdate = rapportPharmacienRepository.findAll().size();

        // Update the rapportPharmacien
        RapportPharmacien updatedRapportPharmacien = rapportPharmacienRepository.findById(rapportPharmacien.getId()).get();
        // Disconnect from session so that the updates on updatedRapportPharmacien are not directly saved in db
        em.detach(updatedRapportPharmacien);
        updatedRapportPharmacien
            .codePatient(UPDATED_CODE_PATIENT)
            .codePS(UPDATED_CODE_PS)
            .facturation(UPDATED_FACTURATION)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);

        restRapportPharmacienMockMvc.perform(put("/api/rapport-pharmaciens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRapportPharmacien)))
            .andExpect(status().isOk());

        // Validate the RapportPharmacien in the database
        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeUpdate);
        RapportPharmacien testRapportPharmacien = rapportPharmacienList.get(rapportPharmacienList.size() - 1);
        assertThat(testRapportPharmacien.getCodePatient()).isEqualTo(UPDATED_CODE_PATIENT);
        assertThat(testRapportPharmacien.getCodePS()).isEqualTo(UPDATED_CODE_PS);
        assertThat(testRapportPharmacien.getFacturation()).isEqualTo(UPDATED_FACTURATION);
        assertThat(testRapportPharmacien.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRapportPharmacien.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingRapportPharmacien() throws Exception {
        int databaseSizeBeforeUpdate = rapportPharmacienRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportPharmacienMockMvc.perform(put("/api/rapport-pharmaciens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rapportPharmacien)))
            .andExpect(status().isBadRequest());

        // Validate the RapportPharmacien in the database
        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRapportPharmacien() throws Exception {
        // Initialize the database
        rapportPharmacienService.save(rapportPharmacien);

        int databaseSizeBeforeDelete = rapportPharmacienRepository.findAll().size();

        // Delete the rapportPharmacien
        restRapportPharmacienMockMvc.perform(delete("/api/rapport-pharmaciens/{id}", rapportPharmacien.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RapportPharmacien> rapportPharmacienList = rapportPharmacienRepository.findAll();
        assertThat(rapportPharmacienList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.FirstCaringApp;
import com.techxel.firstcaring.domain.Pack;
import com.techxel.firstcaring.repository.PackRepository;
import com.techxel.firstcaring.service.PackService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PackResource} REST controller.
 */
@SpringBootTest(classes = FirstCaringApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PackResourceIT {

    private static final String DEFAULT_DENOMMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMMINATION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NMB_DE_PERS = 1;
    private static final Integer UPDATED_NMB_DE_PERS = 2;

    @Autowired
    private PackRepository packRepository;

    @Autowired
    private PackService packService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackMockMvc;

    private Pack pack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pack createEntity(EntityManager em) {
        Pack pack = new Pack()
            .denommination(DEFAULT_DENOMMINATION)
            .prix(DEFAULT_PRIX)
            .description(DEFAULT_DESCRIPTION)
            .nmbDePers(DEFAULT_NMB_DE_PERS);
        return pack;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pack createUpdatedEntity(EntityManager em) {
        Pack pack = new Pack()
            .denommination(UPDATED_DENOMMINATION)
            .prix(UPDATED_PRIX)
            .description(UPDATED_DESCRIPTION)
            .nmbDePers(UPDATED_NMB_DE_PERS);
        return pack;
    }

    @BeforeEach
    public void initTest() {
        pack = createEntity(em);
    }

    @Test
    @Transactional
    public void createPack() throws Exception {
        int databaseSizeBeforeCreate = packRepository.findAll().size();
        // Create the Pack
        restPackMockMvc.perform(post("/api/packs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isCreated());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeCreate + 1);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getDenommination()).isEqualTo(DEFAULT_DENOMMINATION);
        assertThat(testPack.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testPack.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPack.getNmbDePers()).isEqualTo(DEFAULT_NMB_DE_PERS);
    }

    @Test
    @Transactional
    public void createPackWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packRepository.findAll().size();

        // Create the Pack with an existing ID
        pack.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackMockMvc.perform(post("/api/packs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDenomminationIsRequired() throws Exception {
        int databaseSizeBeforeTest = packRepository.findAll().size();
        // set the field null
        pack.setDenommination(null);

        // Create the Pack, which fails.


        restPackMockMvc.perform(post("/api/packs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isBadRequest());

        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixIsRequired() throws Exception {
        int databaseSizeBeforeTest = packRepository.findAll().size();
        // set the field null
        pack.setPrix(null);

        // Create the Pack, which fails.


        restPackMockMvc.perform(post("/api/packs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isBadRequest());

        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNmbDePersIsRequired() throws Exception {
        int databaseSizeBeforeTest = packRepository.findAll().size();
        // set the field null
        pack.setNmbDePers(null);

        // Create the Pack, which fails.


        restPackMockMvc.perform(post("/api/packs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isBadRequest());

        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPacks() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        // Get all the packList
        restPackMockMvc.perform(get("/api/packs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pack.getId().intValue())))
            .andExpect(jsonPath("$.[*].denommination").value(hasItem(DEFAULT_DENOMMINATION)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].nmbDePers").value(hasItem(DEFAULT_NMB_DE_PERS)));
    }
    
    @Test
    @Transactional
    public void getPack() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        // Get the pack
        restPackMockMvc.perform(get("/api/packs/{id}", pack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pack.getId().intValue()))
            .andExpect(jsonPath("$.denommination").value(DEFAULT_DENOMMINATION))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.nmbDePers").value(DEFAULT_NMB_DE_PERS));
    }
    @Test
    @Transactional
    public void getNonExistingPack() throws Exception {
        // Get the pack
        restPackMockMvc.perform(get("/api/packs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePack() throws Exception {
        // Initialize the database
        packService.save(pack);

        int databaseSizeBeforeUpdate = packRepository.findAll().size();

        // Update the pack
        Pack updatedPack = packRepository.findById(pack.getId()).get();
        // Disconnect from session so that the updates on updatedPack are not directly saved in db
        em.detach(updatedPack);
        updatedPack
            .denommination(UPDATED_DENOMMINATION)
            .prix(UPDATED_PRIX)
            .description(UPDATED_DESCRIPTION)
            .nmbDePers(UPDATED_NMB_DE_PERS);

        restPackMockMvc.perform(put("/api/packs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPack)))
            .andExpect(status().isOk());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getDenommination()).isEqualTo(UPDATED_DENOMMINATION);
        assertThat(testPack.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testPack.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPack.getNmbDePers()).isEqualTo(UPDATED_NMB_DE_PERS);
    }

    @Test
    @Transactional
    public void updateNonExistingPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackMockMvc.perform(put("/api/packs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pack)))
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePack() throws Exception {
        // Initialize the database
        packService.save(pack);

        int databaseSizeBeforeDelete = packRepository.findAll().size();

        // Delete the pack
        restPackMockMvc.perform(delete("/api/packs/{id}", pack.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

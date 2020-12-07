package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.FirstCaringApp;
import com.techxel.firstcaring.domain.Assureur;
import com.techxel.firstcaring.repository.AssureurRepository;
import com.techxel.firstcaring.service.AssureurService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.techxel.firstcaring.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.firstcaring.domain.enumeration.Profil;
import com.techxel.firstcaring.domain.enumeration.Sexe;
/**
 * Integration tests for the {@link AssureurResource} REST controller.
 */
@SpringBootTest(classes = FirstCaringApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AssureurResourceIT {

    private static final String DEFAULT_CODE_ASSUREUR = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ASSUREUR = "BBBBBBBBBB";

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
    private AssureurRepository assureurRepository;

    @Mock
    private AssureurRepository assureurRepositoryMock;

    @Mock
    private AssureurService assureurServiceMock;

    @Autowired
    private AssureurService assureurService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssureurMockMvc;

    private Assureur assureur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assureur createEntity(EntityManager em) {
        Assureur assureur = new Assureur()
            .codeAssureur(DEFAULT_CODE_ASSUREUR)
            .profil(DEFAULT_PROFIL)
            .sexe(DEFAULT_SEXE)
            .telephone(DEFAULT_TELEPHONE)
            .createdAt(DEFAULT_CREATED_AT)
            .urlPhoto(DEFAULT_URL_PHOTO)
            .adresse(DEFAULT_ADRESSE);
        return assureur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assureur createUpdatedEntity(EntityManager em) {
        Assureur assureur = new Assureur()
            .codeAssureur(UPDATED_CODE_ASSUREUR)
            .profil(UPDATED_PROFIL)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .urlPhoto(UPDATED_URL_PHOTO)
            .adresse(UPDATED_ADRESSE);
        return assureur;
    }

    @BeforeEach
    public void initTest() {
        assureur = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssureur() throws Exception {
        int databaseSizeBeforeCreate = assureurRepository.findAll().size();
        // Create the Assureur
        restAssureurMockMvc.perform(post("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isCreated());

        // Validate the Assureur in the database
        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeCreate + 1);
        Assureur testAssureur = assureurList.get(assureurList.size() - 1);
        assertThat(testAssureur.getCodeAssureur()).isEqualTo(DEFAULT_CODE_ASSUREUR);
        assertThat(testAssureur.getProfil()).isEqualTo(DEFAULT_PROFIL);
        assertThat(testAssureur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testAssureur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        // assertThat(testAssureur.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAssureur.getUrlPhoto()).isEqualTo(DEFAULT_URL_PHOTO);
        assertThat(testAssureur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    public void createAssureurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assureurRepository.findAll().size();

        // Create the Assureur with an existing ID
        assureur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssureurMockMvc.perform(post("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isBadRequest());

        // Validate the Assureur in the database
        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeAssureurIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureurRepository.findAll().size();
        // set the field null
        assureur.setCodeAssureur(null);

        // Create the Assureur, which fails.


        restAssureurMockMvc.perform(post("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isBadRequest());

        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProfilIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureurRepository.findAll().size();
        // set the field null
        assureur.setProfil(null);

        // Create the Assureur, which fails.


        restAssureurMockMvc.perform(post("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isBadRequest());

        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureurRepository.findAll().size();
        // set the field null
        assureur.setSexe(null);

        // Create the Assureur, which fails.


        restAssureurMockMvc.perform(post("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isBadRequest());

        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureurRepository.findAll().size();
        // set the field null
        assureur.setTelephone(null);

        // Create the Assureur, which fails.


        restAssureurMockMvc.perform(post("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isBadRequest());

        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureurRepository.findAll().size();
        // set the field null
        assureur.setCreatedAt(null);

        // Create the Assureur, which fails.


        restAssureurMockMvc.perform(post("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isBadRequest());

        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = assureurRepository.findAll().size();
        // set the field null
        assureur.setAdresse(null);

        // Create the Assureur, which fails.


        restAssureurMockMvc.perform(post("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isBadRequest());

        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssureurs() throws Exception {
        // Initialize the database
        assureurRepository.saveAndFlush(assureur);

        // Get all the assureurList
        restAssureurMockMvc.perform(get("/api/assureurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assureur.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssureur").value(hasItem(DEFAULT_CODE_ASSUREUR)))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL.toString())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllAssureursWithEagerRelationshipsIsEnabled() throws Exception {
        when(assureurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssureurMockMvc.perform(get("/api/assureurs?eagerload=true"))
            .andExpect(status().isOk());

        verify(assureurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllAssureursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assureurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssureurMockMvc.perform(get("/api/assureurs?eagerload=true"))
            .andExpect(status().isOk());

        verify(assureurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getAssureur() throws Exception {
        // Initialize the database
        assureurRepository.saveAndFlush(assureur);

        // Get the assureur
        restAssureurMockMvc.perform(get("/api/assureurs/{id}", assureur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assureur.getId().intValue()))
            .andExpect(jsonPath("$.codeAssureur").value(DEFAULT_CODE_ASSUREUR))
            .andExpect(jsonPath("$.profil").value(DEFAULT_PROFIL.toString()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.urlPhoto").value(DEFAULT_URL_PHOTO))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE));
    }
    @Test
    @Transactional
    public void getNonExistingAssureur() throws Exception {
        // Get the assureur
        restAssureurMockMvc.perform(get("/api/assureurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssureur() throws Exception {
        // Initialize the database
        assureurService.save(assureur);

        int databaseSizeBeforeUpdate = assureurRepository.findAll().size();

        // Update the assureur
        Assureur updatedAssureur = assureurRepository.findById(assureur.getId()).get();
        // Disconnect from session so that the updates on updatedAssureur are not directly saved in db
        em.detach(updatedAssureur);
        updatedAssureur
            .codeAssureur(UPDATED_CODE_ASSUREUR)
            .profil(UPDATED_PROFIL)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .urlPhoto(UPDATED_URL_PHOTO)
            .adresse(UPDATED_ADRESSE);

        restAssureurMockMvc.perform(put("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAssureur)))
            .andExpect(status().isOk());

        // Validate the Assureur in the database
        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeUpdate);
        Assureur testAssureur = assureurList.get(assureurList.size() - 1);
        assertThat(testAssureur.getCodeAssureur()).isEqualTo(UPDATED_CODE_ASSUREUR);
        assertThat(testAssureur.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testAssureur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testAssureur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAssureur.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAssureur.getUrlPhoto()).isEqualTo(UPDATED_URL_PHOTO);
        assertThat(testAssureur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void updateNonExistingAssureur() throws Exception {
        int databaseSizeBeforeUpdate = assureurRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssureurMockMvc.perform(put("/api/assureurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assureur)))
            .andExpect(status().isBadRequest());

        // Validate the Assureur in the database
        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAssureur() throws Exception {
        // Initialize the database
        assureurService.save(assureur);

        int databaseSizeBeforeDelete = assureurRepository.findAll().size();

        // Delete the assureur
        restAssureurMockMvc.perform(delete("/api/assureurs/{id}", assureur.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assureur> assureurList = assureurRepository.findAll();
        assertThat(assureurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

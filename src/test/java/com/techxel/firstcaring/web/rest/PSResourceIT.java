package com.techxel.firstcaring.web.rest;

import com.techxel.firstcaring.FirstCaringApp;
import com.techxel.firstcaring.domain.PS;
import com.techxel.firstcaring.repository.PSRepository;
import com.techxel.firstcaring.service.PSService;

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
/**
 * Integration tests for the {@link PSResource} REST controller.
 */
@SpringBootTest(classes = FirstCaringApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PSResourceIT {

    private static final String DEFAULT_CODE_PS = "AAAAAAAAAA";
    private static final String UPDATED_CODE_PS = "BBBBBBBBBB";

    private static final Profil DEFAULT_PROFIL = Profil.ASSUREUR;
    private static final Profil UPDATED_PROFIL = Profil.ASSURE;

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_URL_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXPERIENCE = 30;
    private static final Integer UPDATED_EXPERIENCE = 29;

    private static final String DEFAULT_NOM_DE_LETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_DE_LETABLISSEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE_DE_LETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_DE_LETABLISSEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_DE_LETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_DE_LETABLISSEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Autowired
    private PSRepository pSRepository;

    @Mock
    private PSRepository pSRepositoryMock;

    @Mock
    private PSService pSServiceMock;

    @Autowired
    private PSService pSService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPSMockMvc;

    private PS pS;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PS createEntity(EntityManager em) {
        PS pS = new PS()
            .codePS(DEFAULT_CODE_PS)
            .profil(DEFAULT_PROFIL)
            .telephone(DEFAULT_TELEPHONE)
            .createdAt(DEFAULT_CREATED_AT)
            .urlPhoto(DEFAULT_URL_PHOTO)
            .profession(DEFAULT_PROFESSION)
            .experience(DEFAULT_EXPERIENCE)
            .nomDeLetablissement(DEFAULT_NOM_DE_LETABLISSEMENT)
            .telephoneDeLetablissement(DEFAULT_TELEPHONE_DE_LETABLISSEMENT)
            .adresseDeLetablissement(DEFAULT_ADRESSE_DE_LETABLISSEMENT)
            .lienGoogleMapsDeLetablissement(DEFAULT_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT);
        return pS;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PS createUpdatedEntity(EntityManager em) {
        PS pS = new PS()
            .codePS(UPDATED_CODE_PS)
            .profil(UPDATED_PROFIL)
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .urlPhoto(UPDATED_URL_PHOTO)
            .profession(UPDATED_PROFESSION)
            .experience(UPDATED_EXPERIENCE)
            .nomDeLetablissement(UPDATED_NOM_DE_LETABLISSEMENT)
            .telephoneDeLetablissement(UPDATED_TELEPHONE_DE_LETABLISSEMENT)
            .adresseDeLetablissement(UPDATED_ADRESSE_DE_LETABLISSEMENT)
            .lienGoogleMapsDeLetablissement(UPDATED_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT);
        return pS;
    }

    @BeforeEach
    public void initTest() {
        pS = createEntity(em);
    }

    @Test
    @Transactional
    public void createPS() throws Exception {
        int databaseSizeBeforeCreate = pSRepository.findAll().size();
        // Create the PS
        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isCreated());

        // Validate the PS in the database
        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeCreate + 1);
        PS testPS = pSList.get(pSList.size() - 1);
        assertThat(testPS.getCodePS()).isEqualTo(DEFAULT_CODE_PS);
        assertThat(testPS.getProfil()).isEqualTo(DEFAULT_PROFIL);
        assertThat(testPS.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testPS.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPS.getUrlPhoto()).isEqualTo(DEFAULT_URL_PHOTO);
        assertThat(testPS.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testPS.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testPS.getNomDeLetablissement()).isEqualTo(DEFAULT_NOM_DE_LETABLISSEMENT);
        assertThat(testPS.getTelephoneDeLetablissement()).isEqualTo(DEFAULT_TELEPHONE_DE_LETABLISSEMENT);
        assertThat(testPS.getAdresseDeLetablissement()).isEqualTo(DEFAULT_ADRESSE_DE_LETABLISSEMENT);
        assertThat(testPS.getLienGoogleMapsDeLetablissement()).isEqualTo(DEFAULT_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT);
    }

    @Test
    @Transactional
    public void createPSWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pSRepository.findAll().size();

        // Create the PS with an existing ID
        pS.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        // Validate the PS in the database
        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodePSIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setCodePS(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProfilIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setProfil(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setTelephone(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setCreatedAt(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProfessionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setProfession(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExperienceIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setExperience(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomDeLetablissementIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setNomDeLetablissement(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneDeLetablissementIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setTelephoneDeLetablissement(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseDeLetablissementIsRequired() throws Exception {
        int databaseSizeBeforeTest = pSRepository.findAll().size();
        // set the field null
        pS.setAdresseDeLetablissement(null);

        // Create the PS, which fails.


        restPSMockMvc.perform(post("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPS() throws Exception {
        // Initialize the database
        pSRepository.saveAndFlush(pS);

        // Get all the pSList
        restPSMockMvc.perform(get("/api/ps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pS.getId().intValue())))
            .andExpect(jsonPath("$.[*].codePS").value(hasItem(DEFAULT_CODE_PS)))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].urlPhoto").value(hasItem(DEFAULT_URL_PHOTO)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].nomDeLetablissement").value(hasItem(DEFAULT_NOM_DE_LETABLISSEMENT)))
            .andExpect(jsonPath("$.[*].telephoneDeLetablissement").value(hasItem(DEFAULT_TELEPHONE_DE_LETABLISSEMENT)))
            .andExpect(jsonPath("$.[*].adresseDeLetablissement").value(hasItem(DEFAULT_ADRESSE_DE_LETABLISSEMENT)))
            .andExpect(jsonPath("$.[*].lienGoogleMapsDeLetablissement").value(hasItem(DEFAULT_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPSWithEagerRelationshipsIsEnabled() throws Exception {
        when(pSServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPSMockMvc.perform(get("/api/ps?eagerload=true"))
            .andExpect(status().isOk());

        verify(pSServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pSServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPSMockMvc.perform(get("/api/ps?eagerload=true"))
            .andExpect(status().isOk());

        verify(pSServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPS() throws Exception {
        // Initialize the database
        pSRepository.saveAndFlush(pS);

        // Get the pS
        restPSMockMvc.perform(get("/api/ps/{id}", pS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pS.getId().intValue()))
            .andExpect(jsonPath("$.codePS").value(DEFAULT_CODE_PS))
            .andExpect(jsonPath("$.profil").value(DEFAULT_PROFIL.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.urlPhoto").value(DEFAULT_URL_PHOTO))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE))
            .andExpect(jsonPath("$.nomDeLetablissement").value(DEFAULT_NOM_DE_LETABLISSEMENT))
            .andExpect(jsonPath("$.telephoneDeLetablissement").value(DEFAULT_TELEPHONE_DE_LETABLISSEMENT))
            .andExpect(jsonPath("$.adresseDeLetablissement").value(DEFAULT_ADRESSE_DE_LETABLISSEMENT))
            .andExpect(jsonPath("$.lienGoogleMapsDeLetablissement").value(DEFAULT_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT));
    }
    @Test
    @Transactional
    public void getNonExistingPS() throws Exception {
        // Get the pS
        restPSMockMvc.perform(get("/api/ps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePS() throws Exception {
        // Initialize the database
        pSService.save(pS);

        int databaseSizeBeforeUpdate = pSRepository.findAll().size();

        // Update the pS
        PS updatedPS = pSRepository.findById(pS.getId()).get();
        // Disconnect from session so that the updates on updatedPS are not directly saved in db
        em.detach(updatedPS);
        updatedPS
            .codePS(UPDATED_CODE_PS)
            .profil(UPDATED_PROFIL)
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .urlPhoto(UPDATED_URL_PHOTO)
            .profession(UPDATED_PROFESSION)
            .experience(UPDATED_EXPERIENCE)
            .nomDeLetablissement(UPDATED_NOM_DE_LETABLISSEMENT)
            .telephoneDeLetablissement(UPDATED_TELEPHONE_DE_LETABLISSEMENT)
            .adresseDeLetablissement(UPDATED_ADRESSE_DE_LETABLISSEMENT)
            .lienGoogleMapsDeLetablissement(UPDATED_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT);

        restPSMockMvc.perform(put("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPS)))
            .andExpect(status().isOk());

        // Validate the PS in the database
        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeUpdate);
        PS testPS = pSList.get(pSList.size() - 1);
        assertThat(testPS.getCodePS()).isEqualTo(UPDATED_CODE_PS);
        assertThat(testPS.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testPS.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testPS.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPS.getUrlPhoto()).isEqualTo(UPDATED_URL_PHOTO);
        assertThat(testPS.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testPS.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testPS.getNomDeLetablissement()).isEqualTo(UPDATED_NOM_DE_LETABLISSEMENT);
        assertThat(testPS.getTelephoneDeLetablissement()).isEqualTo(UPDATED_TELEPHONE_DE_LETABLISSEMENT);
        assertThat(testPS.getAdresseDeLetablissement()).isEqualTo(UPDATED_ADRESSE_DE_LETABLISSEMENT);
        assertThat(testPS.getLienGoogleMapsDeLetablissement()).isEqualTo(UPDATED_LIEN_GOOGLE_MAPS_DE_LETABLISSEMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingPS() throws Exception {
        int databaseSizeBeforeUpdate = pSRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPSMockMvc.perform(put("/api/ps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pS)))
            .andExpect(status().isBadRequest());

        // Validate the PS in the database
        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePS() throws Exception {
        // Initialize the database
        pSService.save(pS);

        int databaseSizeBeforeDelete = pSRepository.findAll().size();

        // Delete the pS
        restPSMockMvc.perform(delete("/api/ps/{id}", pS.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PS> pSList = pSRepository.findAll();
        assertThat(pSList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

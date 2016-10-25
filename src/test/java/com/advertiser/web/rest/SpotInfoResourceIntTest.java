package com.advertiser.web.rest;

import com.advertiser.AdvertiserApp;

import com.advertiser.domain.SpotInfo;
import com.advertiser.repository.SpotInfoRepository;
import com.advertiser.service.dto.SpotInfoDTO;
import com.advertiser.service.mapper.SpotInfoMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SpotInfoResource REST controller.
 *
 * @see SpotInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvertiserApp.class)
public class SpotInfoResourceIntTest {

    private static final Double DEFAULT_LENGTH = 1D;
    private static final Double UPDATED_LENGTH = 2D;

    private static final String DEFAULT_FILE_PATH = "AAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBB";

    private static final String DEFAULT_PRODUCER = "AAAAA";
    private static final String UPDATED_PRODUCER = "BBBBB";

    private static final String DEFAULT_SCENARIO_AUTHOR = "AAAAA";
    private static final String UPDATED_SCENARIO_AUTHOR = "BBBBB";

    private static final String DEFAULT_SOUND_AUTHOR = "AAAAA";
    private static final String UPDATED_SOUND_AUTHOR = "BBBBB";

    private static final String DEFAULT_PERFORMER = "AAAAA";
    private static final String UPDATED_PERFORMER = "BBBBB";

    private static final String DEFAULT_MUSIC = "AAAAA";
    private static final String UPDATED_MUSIC = "BBBBB";

    @Inject
    private SpotInfoRepository spotInfoRepository;

    @Inject
    private SpotInfoMapper spotInfoMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSpotInfoMockMvc;

    private SpotInfo spotInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpotInfoResource spotInfoResource = new SpotInfoResource();
        ReflectionTestUtils.setField(spotInfoResource, "spotInfoRepository", spotInfoRepository);
        ReflectionTestUtils.setField(spotInfoResource, "spotInfoMapper", spotInfoMapper);
        this.restSpotInfoMockMvc = MockMvcBuilders.standaloneSetup(spotInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpotInfo createEntity(EntityManager em) {
        SpotInfo spotInfo = new SpotInfo()
                .length(DEFAULT_LENGTH)
                .filePath(DEFAULT_FILE_PATH)
                .producer(DEFAULT_PRODUCER)
                .scenarioAuthor(DEFAULT_SCENARIO_AUTHOR)
                .soundAuthor(DEFAULT_SOUND_AUTHOR)
                .performer(DEFAULT_PERFORMER)
                .music(DEFAULT_MUSIC);
        return spotInfo;
    }

    @Before
    public void initTest() {
        spotInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpotInfo() throws Exception {
        int databaseSizeBeforeCreate = spotInfoRepository.findAll().size();

        // Create the SpotInfo
        SpotInfoDTO spotInfoDTO = spotInfoMapper.spotInfoToSpotInfoDTO(spotInfo);

        restSpotInfoMockMvc.perform(post("/api/spot-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spotInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the SpotInfo in the database
        List<SpotInfo> spotInfos = spotInfoRepository.findAll();
        assertThat(spotInfos).hasSize(databaseSizeBeforeCreate + 1);
        SpotInfo testSpotInfo = spotInfos.get(spotInfos.size() - 1);
        assertThat(testSpotInfo.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testSpotInfo.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testSpotInfo.getProducer()).isEqualTo(DEFAULT_PRODUCER);
        assertThat(testSpotInfo.getScenarioAuthor()).isEqualTo(DEFAULT_SCENARIO_AUTHOR);
        assertThat(testSpotInfo.getSoundAuthor()).isEqualTo(DEFAULT_SOUND_AUTHOR);
        assertThat(testSpotInfo.getPerformer()).isEqualTo(DEFAULT_PERFORMER);
        assertThat(testSpotInfo.getMusic()).isEqualTo(DEFAULT_MUSIC);
    }

    @Test
    @Transactional
    public void getAllSpotInfos() throws Exception {
        // Initialize the database
        spotInfoRepository.saveAndFlush(spotInfo);

        // Get all the spotInfos
        restSpotInfoMockMvc.perform(get("/api/spot-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(spotInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
                .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
                .andExpect(jsonPath("$.[*].producer").value(hasItem(DEFAULT_PRODUCER.toString())))
                .andExpect(jsonPath("$.[*].scenarioAuthor").value(hasItem(DEFAULT_SCENARIO_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].soundAuthor").value(hasItem(DEFAULT_SOUND_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].performer").value(hasItem(DEFAULT_PERFORMER.toString())))
                .andExpect(jsonPath("$.[*].music").value(hasItem(DEFAULT_MUSIC.toString())));
    }

    @Test
    @Transactional
    public void getSpotInfo() throws Exception {
        // Initialize the database
        spotInfoRepository.saveAndFlush(spotInfo);

        // Get the spotInfo
        restSpotInfoMockMvc.perform(get("/api/spot-infos/{id}", spotInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spotInfo.getId().intValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.producer").value(DEFAULT_PRODUCER.toString()))
            .andExpect(jsonPath("$.scenarioAuthor").value(DEFAULT_SCENARIO_AUTHOR.toString()))
            .andExpect(jsonPath("$.soundAuthor").value(DEFAULT_SOUND_AUTHOR.toString()))
            .andExpect(jsonPath("$.performer").value(DEFAULT_PERFORMER.toString()))
            .andExpect(jsonPath("$.music").value(DEFAULT_MUSIC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpotInfo() throws Exception {
        // Get the spotInfo
        restSpotInfoMockMvc.perform(get("/api/spot-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpotInfo() throws Exception {
        // Initialize the database
        spotInfoRepository.saveAndFlush(spotInfo);
        int databaseSizeBeforeUpdate = spotInfoRepository.findAll().size();

        // Update the spotInfo
        SpotInfo updatedSpotInfo = spotInfoRepository.findOne(spotInfo.getId());
        updatedSpotInfo
                .length(UPDATED_LENGTH)
                .filePath(UPDATED_FILE_PATH)
                .producer(UPDATED_PRODUCER)
                .scenarioAuthor(UPDATED_SCENARIO_AUTHOR)
                .soundAuthor(UPDATED_SOUND_AUTHOR)
                .performer(UPDATED_PERFORMER)
                .music(UPDATED_MUSIC);
        SpotInfoDTO spotInfoDTO = spotInfoMapper.spotInfoToSpotInfoDTO(updatedSpotInfo);

        restSpotInfoMockMvc.perform(put("/api/spot-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spotInfoDTO)))
                .andExpect(status().isOk());

        // Validate the SpotInfo in the database
        List<SpotInfo> spotInfos = spotInfoRepository.findAll();
        assertThat(spotInfos).hasSize(databaseSizeBeforeUpdate);
        SpotInfo testSpotInfo = spotInfos.get(spotInfos.size() - 1);
        assertThat(testSpotInfo.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testSpotInfo.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testSpotInfo.getProducer()).isEqualTo(UPDATED_PRODUCER);
        assertThat(testSpotInfo.getScenarioAuthor()).isEqualTo(UPDATED_SCENARIO_AUTHOR);
        assertThat(testSpotInfo.getSoundAuthor()).isEqualTo(UPDATED_SOUND_AUTHOR);
        assertThat(testSpotInfo.getPerformer()).isEqualTo(UPDATED_PERFORMER);
        assertThat(testSpotInfo.getMusic()).isEqualTo(UPDATED_MUSIC);
    }

    @Test
    @Transactional
    public void deleteSpotInfo() throws Exception {
        // Initialize the database
        spotInfoRepository.saveAndFlush(spotInfo);
        int databaseSizeBeforeDelete = spotInfoRepository.findAll().size();

        // Get the spotInfo
        restSpotInfoMockMvc.perform(delete("/api/spot-infos/{id}", spotInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SpotInfo> spotInfos = spotInfoRepository.findAll();
        assertThat(spotInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.advertiser.web.rest;

import com.advertiser.AdvertiserApp;

import com.advertiser.domain.Spot;
import com.advertiser.repository.SpotRepository;
import com.advertiser.service.dto.SpotDTO;
import com.advertiser.service.mapper.SpotMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SpotResource REST controller.
 *
 * @see SpotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvertiserApp.class)
public class SpotResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault()).plusDays(1);
    private static final ZonedDateTime UPDATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_TIME_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATE_TIME);

    @Inject
    private SpotRepository spotRepository;

    @Inject
    private SpotMapper spotMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSpotMockMvc;

    private Spot spot;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpotResource spotResource = new SpotResource();
        ReflectionTestUtils.setField(spotResource, "spotRepository", spotRepository);
        ReflectionTestUtils.setField(spotResource, "spotMapper", spotMapper);
        this.restSpotMockMvc = MockMvcBuilders.standaloneSetup(spotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spot createEntity(EntityManager em) {
        Spot spot = new Spot()
                .dateTime(DEFAULT_DATE_TIME);
        return spot;
    }

    @Before
    public void initTest() {
        spot = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpot() throws Exception {
        int databaseSizeBeforeCreate = spotRepository.findAll().size();

        // Create the Spot
        SpotDTO spotDTO = spotMapper.spotToSpotDTO(spot);

        restSpotMockMvc.perform(post("/api/spots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spotDTO)))
                .andExpect(status().isCreated());

        // Validate the Spot in the database
        List<Spot> spots = spotRepository.findAll();
        assertThat(spots).hasSize(databaseSizeBeforeCreate + 1);
        Spot testSpot = spots.get(spots.size() - 1);
        assertThat(testSpot.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllSpots() throws Exception {
        // Initialize the database
        spotRepository.saveAndFlush(spot);

        // Get all the spots
        restSpotMockMvc.perform(get("/api/spots?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(spot.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME_STR)));
    }

    @Test
    @Transactional
    public void getSpot() throws Exception {
        // Initialize the database
        spotRepository.saveAndFlush(spot);

        // Get the spot
        restSpotMockMvc.perform(get("/api/spots/{id}", spot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spot.getId().intValue()))
            .andExpect(jsonPath("$.dateTime").value(DEFAULT_DATE_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSpot() throws Exception {
        // Get the spot
        restSpotMockMvc.perform(get("/api/spots/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpot() throws Exception {
        // Initialize the database
        spotRepository.saveAndFlush(spot);
        int databaseSizeBeforeUpdate = spotRepository.findAll().size();

        // Update the spot
        Spot updatedSpot = spotRepository.findOne(spot.getId());
        updatedSpot
                .dateTime(UPDATED_DATE_TIME);
        SpotDTO spotDTO = spotMapper.spotToSpotDTO(updatedSpot);

        restSpotMockMvc.perform(put("/api/spots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spotDTO)))
                .andExpect(status().isOk());

        // Validate the Spot in the database
        List<Spot> spots = spotRepository.findAll();
        assertThat(spots).hasSize(databaseSizeBeforeUpdate);
        Spot testSpot = spots.get(spots.size() - 1);
        assertThat(testSpot.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void deleteSpot() throws Exception {
        // Initialize the database
        spotRepository.saveAndFlush(spot);
        int databaseSizeBeforeDelete = spotRepository.findAll().size();

        // Get the spot
        restSpotMockMvc.perform(delete("/api/spots/{id}", spot.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Spot> spots = spotRepository.findAll();
        assertThat(spots).hasSize(databaseSizeBeforeDelete - 1);
    }
}

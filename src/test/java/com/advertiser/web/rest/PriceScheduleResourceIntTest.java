package com.advertiser.web.rest;

import com.advertiser.AdvertiserApp;

import com.advertiser.domain.PriceSchedule;
import com.advertiser.repository.PriceScheduleRepository;
import com.advertiser.service.dto.PriceScheduleDTO;
import com.advertiser.service.mapper.PriceScheduleMapper;

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
 * Test class for the PriceScheduleResource REST controller.
 *
 * @see PriceScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvertiserApp.class)
public class PriceScheduleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_END_DATE);

    @Inject
    private PriceScheduleRepository priceScheduleRepository;

    @Inject
    private PriceScheduleMapper priceScheduleMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPriceScheduleMockMvc;

    private PriceSchedule priceSchedule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PriceScheduleResource priceScheduleResource = new PriceScheduleResource();
        ReflectionTestUtils.setField(priceScheduleResource, "priceScheduleRepository", priceScheduleRepository);
        ReflectionTestUtils.setField(priceScheduleResource, "priceScheduleMapper", priceScheduleMapper);
        this.restPriceScheduleMockMvc = MockMvcBuilders.standaloneSetup(priceScheduleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceSchedule createEntity(EntityManager em) {
        PriceSchedule priceSchedule = new PriceSchedule()
                .name(DEFAULT_NAME)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return priceSchedule;
    }

    @Before
    public void initTest() {
        priceSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createPriceSchedule() throws Exception {
        int databaseSizeBeforeCreate = priceScheduleRepository.findAll().size();

        // Create the PriceSchedule
        PriceScheduleDTO priceScheduleDTO = priceScheduleMapper.priceScheduleToPriceScheduleDTO(priceSchedule);

        restPriceScheduleMockMvc.perform(post("/api/price-schedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priceScheduleDTO)))
                .andExpect(status().isCreated());

        // Validate the PriceSchedule in the database
        List<PriceSchedule> priceSchedules = priceScheduleRepository.findAll();
        assertThat(priceSchedules).hasSize(databaseSizeBeforeCreate + 1);
        PriceSchedule testPriceSchedule = priceSchedules.get(priceSchedules.size() - 1);
        assertThat(testPriceSchedule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPriceSchedule.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPriceSchedule.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPriceSchedules() throws Exception {
        // Initialize the database
        priceScheduleRepository.saveAndFlush(priceSchedule);

        // Get all the priceSchedules
        restPriceScheduleMockMvc.perform(get("/api/price-schedules?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(priceSchedule.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }

    @Test
    @Transactional
    public void getPriceSchedule() throws Exception {
        // Initialize the database
        priceScheduleRepository.saveAndFlush(priceSchedule);

        // Get the priceSchedule
        restPriceScheduleMockMvc.perform(get("/api/price-schedules/{id}", priceSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(priceSchedule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPriceSchedule() throws Exception {
        // Get the priceSchedule
        restPriceScheduleMockMvc.perform(get("/api/price-schedules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePriceSchedule() throws Exception {
        // Initialize the database
        priceScheduleRepository.saveAndFlush(priceSchedule);
        int databaseSizeBeforeUpdate = priceScheduleRepository.findAll().size();

        // Update the priceSchedule
        PriceSchedule updatedPriceSchedule = priceScheduleRepository.findOne(priceSchedule.getId());
        updatedPriceSchedule
                .name(UPDATED_NAME)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);
        PriceScheduleDTO priceScheduleDTO = priceScheduleMapper.priceScheduleToPriceScheduleDTO(updatedPriceSchedule);

        restPriceScheduleMockMvc.perform(put("/api/price-schedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priceScheduleDTO)))
                .andExpect(status().isOk());

        // Validate the PriceSchedule in the database
        List<PriceSchedule> priceSchedules = priceScheduleRepository.findAll();
        assertThat(priceSchedules).hasSize(databaseSizeBeforeUpdate);
        PriceSchedule testPriceSchedule = priceSchedules.get(priceSchedules.size() - 1);
        assertThat(testPriceSchedule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriceSchedule.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPriceSchedule.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deletePriceSchedule() throws Exception {
        // Initialize the database
        priceScheduleRepository.saveAndFlush(priceSchedule);
        int databaseSizeBeforeDelete = priceScheduleRepository.findAll().size();

        // Get the priceSchedule
        restPriceScheduleMockMvc.perform(delete("/api/price-schedules/{id}", priceSchedule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PriceSchedule> priceSchedules = priceScheduleRepository.findAll();
        assertThat(priceSchedules).hasSize(databaseSizeBeforeDelete - 1);
    }
}

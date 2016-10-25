package com.advertiser.web.rest;

import com.advertiser.AdvertiserApp;

import com.advertiser.domain.PriceScheduleHour;
import com.advertiser.repository.PriceScheduleHourRepository;
import com.advertiser.service.dto.PriceScheduleHourDTO;
import com.advertiser.service.mapper.PriceScheduleHourMapper;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PriceScheduleHourResource REST controller.
 *
 * @see PriceScheduleHourResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvertiserApp.class)
public class PriceScheduleHourResourceIntTest {

    private static final Integer DEFAULT_HOUR = 1;
    private static final Integer UPDATED_HOUR = 2;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    @Inject
    private PriceScheduleHourRepository priceScheduleHourRepository;

    @Inject
    private PriceScheduleHourMapper priceScheduleHourMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPriceScheduleHourMockMvc;

    private PriceScheduleHour priceScheduleHour;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PriceScheduleHourResource priceScheduleHourResource = new PriceScheduleHourResource();
        ReflectionTestUtils.setField(priceScheduleHourResource, "priceScheduleHourRepository", priceScheduleHourRepository);
        ReflectionTestUtils.setField(priceScheduleHourResource, "priceScheduleHourMapper", priceScheduleHourMapper);
        this.restPriceScheduleHourMockMvc = MockMvcBuilders.standaloneSetup(priceScheduleHourResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceScheduleHour createEntity(EntityManager em) {
        PriceScheduleHour priceScheduleHour = new PriceScheduleHour()
                .hour(DEFAULT_HOUR)
                .price(DEFAULT_PRICE);
        return priceScheduleHour;
    }

    @Before
    public void initTest() {
        priceScheduleHour = createEntity(em);
    }

    @Test
    @Transactional
    public void createPriceScheduleHour() throws Exception {
        int databaseSizeBeforeCreate = priceScheduleHourRepository.findAll().size();

        // Create the PriceScheduleHour
        PriceScheduleHourDTO priceScheduleHourDTO = priceScheduleHourMapper.priceScheduleHourToPriceScheduleHourDTO(priceScheduleHour);

        restPriceScheduleHourMockMvc.perform(post("/api/price-schedule-hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priceScheduleHourDTO)))
                .andExpect(status().isCreated());

        // Validate the PriceScheduleHour in the database
        List<PriceScheduleHour> priceScheduleHours = priceScheduleHourRepository.findAll();
        assertThat(priceScheduleHours).hasSize(databaseSizeBeforeCreate + 1);
        PriceScheduleHour testPriceScheduleHour = priceScheduleHours.get(priceScheduleHours.size() - 1);
        assertThat(testPriceScheduleHour.getHour()).isEqualTo(DEFAULT_HOUR);
        assertThat(testPriceScheduleHour.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void getAllPriceScheduleHours() throws Exception {
        // Initialize the database
        priceScheduleHourRepository.saveAndFlush(priceScheduleHour);

        // Get all the priceScheduleHours
        restPriceScheduleHourMockMvc.perform(get("/api/price-schedule-hours?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(priceScheduleHour.getId().intValue())))
                .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getPriceScheduleHour() throws Exception {
        // Initialize the database
        priceScheduleHourRepository.saveAndFlush(priceScheduleHour);

        // Get the priceScheduleHour
        restPriceScheduleHourMockMvc.perform(get("/api/price-schedule-hours/{id}", priceScheduleHour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(priceScheduleHour.getId().intValue()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPriceScheduleHour() throws Exception {
        // Get the priceScheduleHour
        restPriceScheduleHourMockMvc.perform(get("/api/price-schedule-hours/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePriceScheduleHour() throws Exception {
        // Initialize the database
        priceScheduleHourRepository.saveAndFlush(priceScheduleHour);
        int databaseSizeBeforeUpdate = priceScheduleHourRepository.findAll().size();

        // Update the priceScheduleHour
        PriceScheduleHour updatedPriceScheduleHour = priceScheduleHourRepository.findOne(priceScheduleHour.getId());
        updatedPriceScheduleHour
                .hour(UPDATED_HOUR)
                .price(UPDATED_PRICE);
        PriceScheduleHourDTO priceScheduleHourDTO = priceScheduleHourMapper.priceScheduleHourToPriceScheduleHourDTO(updatedPriceScheduleHour);

        restPriceScheduleHourMockMvc.perform(put("/api/price-schedule-hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priceScheduleHourDTO)))
                .andExpect(status().isOk());

        // Validate the PriceScheduleHour in the database
        List<PriceScheduleHour> priceScheduleHours = priceScheduleHourRepository.findAll();
        assertThat(priceScheduleHours).hasSize(databaseSizeBeforeUpdate);
        PriceScheduleHour testPriceScheduleHour = priceScheduleHours.get(priceScheduleHours.size() - 1);
        assertThat(testPriceScheduleHour.getHour()).isEqualTo(UPDATED_HOUR);
        assertThat(testPriceScheduleHour.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deletePriceScheduleHour() throws Exception {
        // Initialize the database
        priceScheduleHourRepository.saveAndFlush(priceScheduleHour);
        int databaseSizeBeforeDelete = priceScheduleHourRepository.findAll().size();

        // Get the priceScheduleHour
        restPriceScheduleHourMockMvc.perform(delete("/api/price-schedule-hours/{id}", priceScheduleHour.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PriceScheduleHour> priceScheduleHours = priceScheduleHourRepository.findAll();
        assertThat(priceScheduleHours).hasSize(databaseSizeBeforeDelete - 1);
    }
}

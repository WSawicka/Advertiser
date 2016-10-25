package com.advertiser.web.rest;

import com.advertiser.AdvertiserApp;

import com.advertiser.domain.Hour;
import com.advertiser.repository.HourRepository;
import com.advertiser.service.dto.HourDTO;
import com.advertiser.service.mapper.HourMapper;

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
 * Test class for the HourResource REST controller.
 *
 * @see HourResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvertiserApp.class)
public class HourResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Inject
    private HourRepository hourRepository;

    @Inject
    private HourMapper hourMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHourMockMvc;

    private Hour hour;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HourResource hourResource = new HourResource();
        ReflectionTestUtils.setField(hourResource, "hourRepository", hourRepository);
        ReflectionTestUtils.setField(hourResource, "hourMapper", hourMapper);
        this.restHourMockMvc = MockMvcBuilders.standaloneSetup(hourResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hour createEntity(EntityManager em) {
        Hour hour = new Hour()
                .number(DEFAULT_NUMBER);
        return hour;
    }

    @Before
    public void initTest() {
        hour = createEntity(em);
    }

    @Test
    @Transactional
    public void createHour() throws Exception {
        int databaseSizeBeforeCreate = hourRepository.findAll().size();

        // Create the Hour
        HourDTO hourDTO = hourMapper.hourToHourDTO(hour);

        restHourMockMvc.perform(post("/api/hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hourDTO)))
                .andExpect(status().isCreated());

        // Validate the Hour in the database
        List<Hour> hours = hourRepository.findAll();
        assertThat(hours).hasSize(databaseSizeBeforeCreate + 1);
        Hour testHour = hours.get(hours.size() - 1);
        assertThat(testHour.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllHours() throws Exception {
        // Initialize the database
        hourRepository.saveAndFlush(hour);

        // Get all the hours
        restHourMockMvc.perform(get("/api/hours?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hour.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void getHour() throws Exception {
        // Initialize the database
        hourRepository.saveAndFlush(hour);

        // Get the hour
        restHourMockMvc.perform(get("/api/hours/{id}", hour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hour.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingHour() throws Exception {
        // Get the hour
        restHourMockMvc.perform(get("/api/hours/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHour() throws Exception {
        // Initialize the database
        hourRepository.saveAndFlush(hour);
        int databaseSizeBeforeUpdate = hourRepository.findAll().size();

        // Update the hour
        Hour updatedHour = hourRepository.findOne(hour.getId());
        updatedHour
                .number(UPDATED_NUMBER);
        HourDTO hourDTO = hourMapper.hourToHourDTO(updatedHour);

        restHourMockMvc.perform(put("/api/hours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hourDTO)))
                .andExpect(status().isOk());

        // Validate the Hour in the database
        List<Hour> hours = hourRepository.findAll();
        assertThat(hours).hasSize(databaseSizeBeforeUpdate);
        Hour testHour = hours.get(hours.size() - 1);
        assertThat(testHour.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void deleteHour() throws Exception {
        // Initialize the database
        hourRepository.saveAndFlush(hour);
        int databaseSizeBeforeDelete = hourRepository.findAll().size();

        // Get the hour
        restHourMockMvc.perform(delete("/api/hours/{id}", hour.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hour> hours = hourRepository.findAll();
        assertThat(hours).hasSize(databaseSizeBeforeDelete - 1);
    }
}

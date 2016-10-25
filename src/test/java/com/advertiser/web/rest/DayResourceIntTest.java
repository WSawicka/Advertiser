package com.advertiser.web.rest;

import com.advertiser.AdvertiserApp;

import com.advertiser.domain.Day;
import com.advertiser.repository.DayRepository;
import com.advertiser.service.dto.DayDTO;
import com.advertiser.service.mapper.DayMapper;

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

import com.advertiser.domain.enumeration.DayName;
/**
 * Test class for the DayResource REST controller.
 *
 * @see DayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvertiserApp.class)
public class DayResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final DayName DEFAULT_DAY_NAME = DayName.MONDAY;
    private static final DayName UPDATED_DAY_NAME = DayName.TUESDAY;

    @Inject
    private DayRepository dayRepository;

    @Inject
    private DayMapper dayMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDayMockMvc;

    private Day day;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DayResource dayResource = new DayResource();
        ReflectionTestUtils.setField(dayResource, "dayRepository", dayRepository);
        ReflectionTestUtils.setField(dayResource, "dayMapper", dayMapper);
        this.restDayMockMvc = MockMvcBuilders.standaloneSetup(dayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Day createEntity(EntityManager em) {
        Day day = new Day()
                .number(DEFAULT_NUMBER)
                .dayName(DEFAULT_DAY_NAME);
        return day;
    }

    @Before
    public void initTest() {
        day = createEntity(em);
    }

    @Test
    @Transactional
    public void createDay() throws Exception {
        int databaseSizeBeforeCreate = dayRepository.findAll().size();

        // Create the Day
        DayDTO dayDTO = dayMapper.dayToDayDTO(day);

        restDayMockMvc.perform(post("/api/days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dayDTO)))
                .andExpect(status().isCreated());

        // Validate the Day in the database
        List<Day> days = dayRepository.findAll();
        assertThat(days).hasSize(databaseSizeBeforeCreate + 1);
        Day testDay = days.get(days.size() - 1);
        assertThat(testDay.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testDay.getDayName()).isEqualTo(DEFAULT_DAY_NAME);
    }

    @Test
    @Transactional
    public void getAllDays() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get all the days
        restDayMockMvc.perform(get("/api/days?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(day.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].dayName").value(hasItem(DEFAULT_DAY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get the day
        restDayMockMvc.perform(get("/api/days/{id}", day.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(day.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.dayName").value(DEFAULT_DAY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDay() throws Exception {
        // Get the day
        restDayMockMvc.perform(get("/api/days/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day
        Day updatedDay = dayRepository.findOne(day.getId());
        updatedDay
                .number(UPDATED_NUMBER)
                .dayName(UPDATED_DAY_NAME);
        DayDTO dayDTO = dayMapper.dayToDayDTO(updatedDay);

        restDayMockMvc.perform(put("/api/days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dayDTO)))
                .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> days = dayRepository.findAll();
        assertThat(days).hasSize(databaseSizeBeforeUpdate);
        Day testDay = days.get(days.size() - 1);
        assertThat(testDay.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDay.getDayName()).isEqualTo(UPDATED_DAY_NAME);
    }

    @Test
    @Transactional
    public void deleteDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);
        int databaseSizeBeforeDelete = dayRepository.findAll().size();

        // Get the day
        restDayMockMvc.perform(delete("/api/days/{id}", day.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Day> days = dayRepository.findAll();
        assertThat(days).hasSize(databaseSizeBeforeDelete - 1);
    }
}

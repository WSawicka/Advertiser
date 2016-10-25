package com.advertiser.web.rest;

import com.advertiser.AdvertiserApp;

import com.advertiser.domain.Campaign;
import com.advertiser.repository.CampaignRepository;
import com.advertiser.service.dto.CampaignDTO;
import com.advertiser.service.mapper.CampaignMapper;

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
 * Test class for the CampaignResource REST controller.
 *
 * @see CampaignResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvertiserApp.class)
public class CampaignResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_NAME_SHORT = "AAAAA";
    private static final String UPDATED_NAME_SHORT = "BBBBB";

    private static final String DEFAULT_PRODUCT = "AAAAA";
    private static final String UPDATED_PRODUCT = "BBBBB";

    private static final Integer DEFAULT_SPOT_AMOUNT = 1;
    private static final Integer UPDATED_SPOT_AMOUNT = 2;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_END_DATE);

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private CampaignMapper campaignMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCampaignMockMvc;

    private Campaign campaign;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CampaignResource campaignResource = new CampaignResource();
        ReflectionTestUtils.setField(campaignResource, "campaignRepository", campaignRepository);
        ReflectionTestUtils.setField(campaignResource, "campaignMapper", campaignMapper);
        this.restCampaignMockMvc = MockMvcBuilders.standaloneSetup(campaignResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campaign createEntity(EntityManager em) {
        Campaign campaign = new Campaign()
                .name(DEFAULT_NAME)
                .nameShort(DEFAULT_NAME_SHORT)
                .product(DEFAULT_PRODUCT)
                .spotAmount(DEFAULT_SPOT_AMOUNT)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return campaign;
    }

    @Before
    public void initTest() {
        campaign = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampaign() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.campaignToCampaignDTO(campaign);

        restCampaignMockMvc.perform(post("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
                .andExpect(status().isCreated());

        // Validate the Campaign in the database
        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeCreate + 1);
        Campaign testCampaign = campaigns.get(campaigns.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCampaign.getNameShort()).isEqualTo(DEFAULT_NAME_SHORT);
        assertThat(testCampaign.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testCampaign.getSpotAmount()).isEqualTo(DEFAULT_SPOT_AMOUNT);
        assertThat(testCampaign.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCampaigns() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaigns
        restCampaignMockMvc.perform(get("/api/campaigns?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].nameShort").value(hasItem(DEFAULT_NAME_SHORT.toString())))
                .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT.toString())))
                .andExpect(jsonPath("$.[*].spotAmount").value(hasItem(DEFAULT_SPOT_AMOUNT)))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }

    @Test
    @Transactional
    public void getCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", campaign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(campaign.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.nameShort").value(DEFAULT_NAME_SHORT.toString()))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT.toString()))
            .andExpect(jsonPath("$.spotAmount").value(DEFAULT_SPOT_AMOUNT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCampaign() throws Exception {
        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Update the campaign
        Campaign updatedCampaign = campaignRepository.findOne(campaign.getId());
        updatedCampaign
                .name(UPDATED_NAME)
                .nameShort(UPDATED_NAME_SHORT)
                .product(UPDATED_PRODUCT)
                .spotAmount(UPDATED_SPOT_AMOUNT)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);
        CampaignDTO campaignDTO = campaignMapper.campaignToCampaignDTO(updatedCampaign);

        restCampaignMockMvc.perform(put("/api/campaigns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
                .andExpect(status().isOk());

        // Validate the Campaign in the database
        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaigns.get(campaigns.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCampaign.getNameShort()).isEqualTo(UPDATED_NAME_SHORT);
        assertThat(testCampaign.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testCampaign.getSpotAmount()).isEqualTo(UPDATED_SPOT_AMOUNT);
        assertThat(testCampaign.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);
        int databaseSizeBeforeDelete = campaignRepository.findAll().size();

        // Get the campaign
        restCampaignMockMvc.perform(delete("/api/campaigns/{id}", campaign.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Campaign> campaigns = campaignRepository.findAll();
        assertThat(campaigns).hasSize(databaseSizeBeforeDelete - 1);
    }
}

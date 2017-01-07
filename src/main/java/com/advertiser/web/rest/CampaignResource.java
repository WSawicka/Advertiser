package com.advertiser.web.rest;

import com.advertiser.domain.enumeration.CampaignBusiness;
import com.advertiser.domain.enumeration.CampaignState;
import com.advertiser.repository.HourRepository;
import com.advertiser.repository.SpotRepository;
import com.advertiser.service.DayService;
import com.advertiser.service.ReportService;
import com.advertiser.service.dto.DayDTO;
import com.advertiser.service.dto.SpotDTO;
import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.Campaign;

import com.advertiser.repository.CampaignRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.CampaignDTO;
import com.advertiser.service.mapper.CampaignMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Campaign.
 */
@RestController
@RequestMapping("/api")
public class CampaignResource {

    private final Logger log = LoggerFactory.getLogger(CampaignResource.class);

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private HourRepository hourRepository;

    @Inject
    private SpotResource spotResource;

    @Inject
    private CampaignMapper campaignMapper;

    @Inject
    private DayService dayService;

    @Inject
    private ReportService reportService;

    /**
     * POST  /campaigns : Create a new campaign.
     *
     * @param campaignDTO the campaignDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new campaignDTO, or with status 400 (Bad Request) if the campaign has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/campaigns",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CampaignDTO> createCampaign(@RequestBody CampaignDTO campaignDTO) throws URISyntaxException {
        log.debug("REST request to save Campaign : {}", campaignDTO);
        if (campaignDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("campaign", "idexists", "A new campaign cannot already have an ID")).body(null);
        }
        Campaign campaign = campaignMapper.campaignDTOToCampaign(campaignDTO);
        campaign = campaignRepository.save(campaign);
        CampaignDTO result = campaignMapper.campaignToCampaignDTO(campaign);
        result.setId(campaign.getId());
        return ResponseEntity.created(new URI("/api/campaigns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("campaign", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /campaigns : Updates an existing campaign.
     *
     * @param campaignDTO the campaignDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated campaignDTO,
     * or with status 400 (Bad Request) if the campaignDTO is not valid,
     * or with status 500 (Internal Server Error) if the campaignDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/campaigns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CampaignDTO> updateCampaign(@RequestBody CampaignDTO campaignDTO) throws URISyntaxException {
        log.debug("REST request to update Campaign : {}", campaignDTO);
        if (campaignDTO.getId() == null) {
            return createCampaign(campaignDTO);
        }
        Campaign campaign = campaignMapper.campaignDTOToCampaign(campaignDTO);
        campaign = campaignRepository.save(campaign);
        CampaignDTO result = campaignMapper.campaignToCampaignDTO(campaign);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("campaign", campaignDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /campaigns : get all the campaigns.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of campaigns in body
     */
    @RequestMapping(value = "/campaigns",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CampaignDTO> getAllCampaigns() {
        log.debug("REST request to get all Campaigns");
        List<Campaign> campaigns = campaignRepository.findAllWithEagerRelationships();
        return campaignMapper.campaignsToCampaignDTOs(campaigns);
    }

    /**
     * GET  /campaigns/:id : get the "id" campaign.
     *
     * @param id the id of the campaignDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the campaignDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/campaigns/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CampaignDTO> getCampaign(@PathVariable Long id) {
        log.debug("REST request to get Campaign : {}", id);
        Campaign campaign = campaignRepository.findOneWithEagerRelationships(id);
        CampaignDTO campaignDTO = campaignMapper.campaignToCampaignDTO(campaign);
        return Optional.ofNullable(campaignDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/campaign/{campaignId}")
    public CampaignDTO getCampaignWithId(@PathVariable Long campaignId){
        Campaign campaign = campaignRepository.findOneWithEagerRelationships(campaignId);
        return campaignMapper.campaignToCampaignDTO(campaign);
    }

    @GetMapping("/campaignsBefore/dateTime/{dateTime}")
    @Timed
    public List<CampaignDTO> getAvailableCampaigns(@PathVariable String dateTime){
        ZonedDateTime dt = ZonedDateTime.of(LocalDateTime.parse(dateTime), ZonedDateTime.now().getZone());
        List<Campaign> campaigns = campaignRepository.findAllAvailableCampaigns(dt);
        return campaignMapper.campaignsToCampaignDTOs(campaigns);
    }

    @GetMapping("/generate/campaign/check/{hoursPreferred}/{startDate}/{endDate}")
    public List<Integer> checkIfPossibleToGenerateSpots(@PathVariable List<Integer> hoursPreferred,
             @PathVariable String startDate, @PathVariable String endDate){
        List<Integer> results = new ArrayList<>();
        Integer allHours = hourRepository.findAllAvailableHours(startDate, endDate);
        Integer preferredHours = hourRepository.findAllAvailablePreferredHours(startDate, endDate, hoursPreferred);
        results.add(allHours);
        results.add(preferredHours);
        return results;
    }

    @GetMapping("/generate/campaign/{generationForm}/{id}/{toGenerate}/{spotInfoId}/{spotsLimit}/{hoursPreferred}/{peaks}")
    public List<SpotDTO> generateSpots(@PathVariable String generationForm, @PathVariable Long id,
          @PathVariable List<Integer> toGenerate, @PathVariable Long spotInfoId, @PathVariable Integer spotsLimit,
          @PathVariable List<Integer> hoursPreferred, @PathVariable List<Integer> peaks){
        CampaignDTO campaignDTO = campaignMapper.campaignToCampaignDTO(campaignRepository.findOne(id));
        List<SpotDTO> spotsToGenerate = dayService.generateSpotsFor (generationForm,
            campaignMapper.campaignDTOToCampaign(campaignDTO), toGenerate, spotInfoId, spotsLimit, hoursPreferred, peaks);
        return spotsToGenerate;
    }

    @GetMapping("/campaignStates")
    public List<String> getAllCampaignStates(){
        List<CampaignState> states = Arrays.asList(CampaignState.values());
        return states.stream().map(Object::toString).collect(Collectors.toList());
    }

    @GetMapping("/campaignBusinesses")
    public ArrayList getAllCampaignBusinesses(){
        CampaignBusiness[] businesses = CampaignBusiness.values();
        ArrayList result = new ArrayList();
        for(CampaignBusiness business : businesses){
            Map<String, String> temp = new HashMap<>();
            temp.put("title", business.name());
            temp.put("name", business.getName());
            result.add(temp);
        }
        return result;
    }

    @GetMapping("/campaigns/all/withAmounts")
    public List<Map<String, Object>> getAmountOfSpotsWithCampaigns(){
        List<Map<String, Object>> resolve = new ArrayList<>();

        List<Campaign> campaigns = campaignRepository.findAllWithEagerRelationships();
        List<CampaignDTO> campaignsDTO = campaignMapper.campaignsToCampaignDTOs(campaigns);

        resolve.addAll(campaignsDTO.stream().map(c -> spotResource.getMappedCampaign(c)).collect(Collectors.toList()));
        return resolve;
    }

    @GetMapping("/{year}/campaigns/all/withAmounts")
    public List<Map<String, Object>> getAmountOfSpotsWithCampaignsOfYear(@PathVariable Integer year){
        List<Map<String, Object>> resolve = new ArrayList<>();
        ZonedDateTime start = ZonedDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
        ZonedDateTime end = start.plusYears(1).minusDays(1);

        List<Campaign> campaigns = campaignRepository.findAllCampaignsBetween(start, end);
        List<CampaignDTO> campaignsDTO = campaignMapper.campaignsToCampaignDTOs(campaigns);

        resolve.addAll(campaignsDTO.stream().map(c -> spotResource.getMappedCampaign(c)).collect(Collectors.toList()));
        return resolve;
    }

    @GetMapping("/report/{year}")
    public Object getReportFrom(@PathVariable Integer year){
        return reportService.getReportOf(year);
    }

    /**
     * DELETE  /campaigns/:id : delete the "id" campaign.
     *
     * @param id the id of the campaignDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/campaigns/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        log.debug("REST request to delete Campaign : {}", id);
        campaignRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("campaign", id.toString())).build();
    }

}

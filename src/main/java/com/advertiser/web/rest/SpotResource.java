package com.advertiser.web.rest;

import com.advertiser.domain.Campaign;
import com.advertiser.domain.enumeration.CampaignBusiness;
import com.advertiser.repository.CampaignRepository;
import com.advertiser.service.dto.CampaignDTO;
import com.advertiser.service.mapper.CampaignMapper;
import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.Spot;

import com.advertiser.repository.SpotRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.SpotDTO;
import com.advertiser.service.mapper.SpotMapper;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Spot.
 */
@RestController
@RequestMapping("/api")
public class SpotResource {

    private final Logger log = LoggerFactory.getLogger(SpotResource.class);

    @Inject
    private CampaignMapper campaignMapper;

    @Inject
    private SpotRepository spotRepository;

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private SpotMapper spotMapper;

    /**
     * POST  /spots : Create a new spot.
     *
     * @param spotDTO the spotDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spotDTO, or with status 400 (Bad Request) if the spot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/spots",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpotDTO> createSpot(@RequestBody SpotDTO spotDTO) throws URISyntaxException {
        log.debug("REST request to save Spot : {}", spotDTO);
        if (spotDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("spot", "idexists", "A new spot cannot already have an ID")).body(null);
        }
        Spot spot = spotMapper.spotDTOToSpot(spotDTO);
        spot = spotRepository.save(spot);
        SpotDTO result = spotMapper.spotToSpotDTO(spot);
        return ResponseEntity.created(new URI("/api/spots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("spot", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spots : Updates an existing spot.
     *
     * @param spotDTO the spotDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spotDTO,
     * or with status 400 (Bad Request) if the spotDTO is not valid,
     * or with status 500 (Internal Server Error) if the spotDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/spots",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpotDTO> updateSpot(@RequestBody SpotDTO spotDTO) throws URISyntaxException {
        log.debug("REST request to update Spot : {}", spotDTO);
        if (spotDTO.getId() == null) {
            return createSpot(spotDTO);
        }
        Spot spot = spotMapper.spotDTOToSpot(spotDTO);
        spot = spotRepository.save(spot);
        SpotDTO result = spotMapper.spotToSpotDTO(spot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("spot", spotDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spots : get all the spots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of spots in body
     */
    @RequestMapping(value = "/spots",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SpotDTO> getAllSpots() {
        log.debug("REST request to get all Spots");
        List<Spot> spots = spotRepository.findAll();
        return spotMapper.spotsToSpotDTOs(spots);
    }

    @GetMapping("/spots/hourId/{hourId}")
    @Timed
    public List<SpotDTO> getAllSpotsByHourId(@PathVariable Long hourId ){
        List<Spot> spots = spotRepository.findAllSpotsByHourId(hourId);
        return spotMapper.spotsToSpotDTOs(spots, campaignMapper);
    }

    @GetMapping("/campaigns/amounts/{userId}")
    public List<Map<String, Object>> getAmountOfSpotsWithCampaignsOfUser(@PathVariable Long userId){
        List<Map<String, Object>> resolve = new ArrayList<>();

        List<Campaign> campaigns = campaignRepository.findAllWithEagerRelationshipsOfUser(userId);
        List<CampaignDTO> campaignsDTO = campaignMapper.campaignsToCampaignDTOs(campaigns);

        resolve.addAll(campaignsDTO.stream().map(this::getMappedCampaign).collect(Collectors.toList()));
        return resolve;
    }

    public Map<String, Object> getMappedCampaign(CampaignDTO campaign){
        Map<String, Object> map = new HashMap<>();
        Long amount = spotRepository.countByCampaignId(campaign.getId());
        map.put("campaign", campaign);
        map.put("amount", amount);
        return map;
    }

    @GetMapping("/campaign/{campaignId}/days/spots")
    public Object getSpotsOrderedFrom(@PathVariable Long campaignId){
        List<DayWithSpotHours> result = new ArrayList<>();
        List<Spot> spots = spotRepository.findAllByCampaignIdOrderByDateTime(campaignId);
        for(Spot spot : spots){
            String day = spot.getDateTime().toLocalDate().toString();
            String time = spot.getDateTime().toLocalTime().toString();
            int index = getIndexOf(result, day);
            if(index != -1){
                result.get(index).getHours().add(time);
            } else {
                DayWithSpotHours dayWithSpotHours = new DayWithSpotHours();
                dayWithSpotHours.setDay(day);
                dayWithSpotHours.hours.add(time);
                result.add(dayWithSpotHours);
            }
        }
        for (DayWithSpotHours day : result){
            Collections.sort(day.hours);
        }
        return result;
    }

    @Getter
    @Setter
    private class DayWithSpotHours{
        private String day;
        private List<String> hours = new ArrayList<>();
    }

    private int getIndexOf(List<DayWithSpotHours> list, String day){
        for(DayWithSpotHours l : list){
            if(l.day.equals(day)) return list.indexOf(l);
        }
        return -1;
    }

    /**
     * GET  /spots/:id : get the "id" spot.
     *
     * @param id the id of the spotDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spotDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/spots/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpotDTO> getSpot(@PathVariable Long id) {
        log.debug("REST request to get Spot : {}", id);
        Spot spot = spotRepository.findOne(id);
        SpotDTO spotDTO = spotMapper.spotToSpotDTO(spot);
        return Optional.ofNullable(spotDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /spots/:id : delete the "id" spot.
     *
     * @param id the id of the spotDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/spots/{id}",
        method = RequestMethod.DELETE)
    @Timed
    public ResponseEntity<Void> deleteSpot(@PathVariable Long id) {
        log.debug("REST request to delete Spot : {}", id);
        spotRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("spot", id.toString())).build();
    }

}

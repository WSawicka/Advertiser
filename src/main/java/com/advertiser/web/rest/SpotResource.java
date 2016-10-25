package com.advertiser.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.Spot;

import com.advertiser.repository.SpotRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.SpotDTO;
import com.advertiser.service.mapper.SpotMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Spot.
 */
@RestController
@RequestMapping("/api")
public class SpotResource {

    private final Logger log = LoggerFactory.getLogger(SpotResource.class);
        
    @Inject
    private SpotRepository spotRepository;

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
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpot(@PathVariable Long id) {
        log.debug("REST request to delete Spot : {}", id);
        spotRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("spot", id.toString())).build();
    }

}
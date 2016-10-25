package com.advertiser.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.Hour;

import com.advertiser.repository.HourRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.HourDTO;
import com.advertiser.service.mapper.HourMapper;
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
 * REST controller for managing Hour.
 */
@RestController
@RequestMapping("/api")
public class HourResource {

    private final Logger log = LoggerFactory.getLogger(HourResource.class);
        
    @Inject
    private HourRepository hourRepository;

    @Inject
    private HourMapper hourMapper;

    /**
     * POST  /hours : Create a new hour.
     *
     * @param hourDTO the hourDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hourDTO, or with status 400 (Bad Request) if the hour has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hours",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HourDTO> createHour(@RequestBody HourDTO hourDTO) throws URISyntaxException {
        log.debug("REST request to save Hour : {}", hourDTO);
        if (hourDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hour", "idexists", "A new hour cannot already have an ID")).body(null);
        }
        Hour hour = hourMapper.hourDTOToHour(hourDTO);
        hour = hourRepository.save(hour);
        HourDTO result = hourMapper.hourToHourDTO(hour);
        return ResponseEntity.created(new URI("/api/hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hour", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hours : Updates an existing hour.
     *
     * @param hourDTO the hourDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hourDTO,
     * or with status 400 (Bad Request) if the hourDTO is not valid,
     * or with status 500 (Internal Server Error) if the hourDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hours",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HourDTO> updateHour(@RequestBody HourDTO hourDTO) throws URISyntaxException {
        log.debug("REST request to update Hour : {}", hourDTO);
        if (hourDTO.getId() == null) {
            return createHour(hourDTO);
        }
        Hour hour = hourMapper.hourDTOToHour(hourDTO);
        hour = hourRepository.save(hour);
        HourDTO result = hourMapper.hourToHourDTO(hour);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hour", hourDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hours : get all the hours.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hours in body
     */
    @RequestMapping(value = "/hours",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HourDTO> getAllHours() {
        log.debug("REST request to get all Hours");
        List<Hour> hours = hourRepository.findAll();
        return hourMapper.hoursToHourDTOs(hours);
    }

    /**
     * GET  /hours/:id : get the "id" hour.
     *
     * @param id the id of the hourDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hourDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/hours/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HourDTO> getHour(@PathVariable Long id) {
        log.debug("REST request to get Hour : {}", id);
        Hour hour = hourRepository.findOne(id);
        HourDTO hourDTO = hourMapper.hourToHourDTO(hour);
        return Optional.ofNullable(hourDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hours/:id : delete the "id" hour.
     *
     * @param id the id of the hourDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/hours/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHour(@PathVariable Long id) {
        log.debug("REST request to delete Hour : {}", id);
        hourRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hour", id.toString())).build();
    }

}

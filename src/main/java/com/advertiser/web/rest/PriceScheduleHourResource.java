package com.advertiser.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.PriceScheduleHour;

import com.advertiser.repository.PriceScheduleHourRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.PriceScheduleHourDTO;
import com.advertiser.service.mapper.PriceScheduleHourMapper;
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
 * REST controller for managing PriceScheduleHour.
 */
@RestController
@RequestMapping("/api")
public class PriceScheduleHourResource {

    private final Logger log = LoggerFactory.getLogger(PriceScheduleHourResource.class);
        
    @Inject
    private PriceScheduleHourRepository priceScheduleHourRepository;

    @Inject
    private PriceScheduleHourMapper priceScheduleHourMapper;

    /**
     * POST  /price-schedule-hours : Create a new priceScheduleHour.
     *
     * @param priceScheduleHourDTO the priceScheduleHourDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new priceScheduleHourDTO, or with status 400 (Bad Request) if the priceScheduleHour has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/price-schedule-hours",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceScheduleHourDTO> createPriceScheduleHour(@RequestBody PriceScheduleHourDTO priceScheduleHourDTO) throws URISyntaxException {
        log.debug("REST request to save PriceScheduleHour : {}", priceScheduleHourDTO);
        if (priceScheduleHourDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("priceScheduleHour", "idexists", "A new priceScheduleHour cannot already have an ID")).body(null);
        }
        PriceScheduleHour priceScheduleHour = priceScheduleHourMapper.priceScheduleHourDTOToPriceScheduleHour(priceScheduleHourDTO);
        priceScheduleHour = priceScheduleHourRepository.save(priceScheduleHour);
        PriceScheduleHourDTO result = priceScheduleHourMapper.priceScheduleHourToPriceScheduleHourDTO(priceScheduleHour);
        return ResponseEntity.created(new URI("/api/price-schedule-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("priceScheduleHour", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /price-schedule-hours : Updates an existing priceScheduleHour.
     *
     * @param priceScheduleHourDTO the priceScheduleHourDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated priceScheduleHourDTO,
     * or with status 400 (Bad Request) if the priceScheduleHourDTO is not valid,
     * or with status 500 (Internal Server Error) if the priceScheduleHourDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/price-schedule-hours",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceScheduleHourDTO> updatePriceScheduleHour(@RequestBody PriceScheduleHourDTO priceScheduleHourDTO) throws URISyntaxException {
        log.debug("REST request to update PriceScheduleHour : {}", priceScheduleHourDTO);
        if (priceScheduleHourDTO.getId() == null) {
            return createPriceScheduleHour(priceScheduleHourDTO);
        }
        PriceScheduleHour priceScheduleHour = priceScheduleHourMapper.priceScheduleHourDTOToPriceScheduleHour(priceScheduleHourDTO);
        priceScheduleHour = priceScheduleHourRepository.save(priceScheduleHour);
        PriceScheduleHourDTO result = priceScheduleHourMapper.priceScheduleHourToPriceScheduleHourDTO(priceScheduleHour);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("priceScheduleHour", priceScheduleHourDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /price-schedule-hours : get all the priceScheduleHours.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of priceScheduleHours in body
     */
    @RequestMapping(value = "/price-schedule-hours",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PriceScheduleHourDTO> getAllPriceScheduleHours() {
        log.debug("REST request to get all PriceScheduleHours");
        List<PriceScheduleHour> priceScheduleHours = priceScheduleHourRepository.findAll();
        return priceScheduleHourMapper.priceScheduleHoursToPriceScheduleHourDTOs(priceScheduleHours);
    }

    /**
     * GET  /price-schedule-hours/:id : get the "id" priceScheduleHour.
     *
     * @param id the id of the priceScheduleHourDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the priceScheduleHourDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/price-schedule-hours/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceScheduleHourDTO> getPriceScheduleHour(@PathVariable Long id) {
        log.debug("REST request to get PriceScheduleHour : {}", id);
        PriceScheduleHour priceScheduleHour = priceScheduleHourRepository.findOne(id);
        PriceScheduleHourDTO priceScheduleHourDTO = priceScheduleHourMapper.priceScheduleHourToPriceScheduleHourDTO(priceScheduleHour);
        return Optional.ofNullable(priceScheduleHourDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /price-schedule-hours/:id : delete the "id" priceScheduleHour.
     *
     * @param id the id of the priceScheduleHourDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/price-schedule-hours/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePriceScheduleHour(@PathVariable Long id) {
        log.debug("REST request to delete PriceScheduleHour : {}", id);
        priceScheduleHourRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("priceScheduleHour", id.toString())).build();
    }

}

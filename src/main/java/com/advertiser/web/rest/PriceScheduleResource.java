package com.advertiser.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.PriceSchedule;

import com.advertiser.repository.PriceScheduleRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.PriceScheduleDTO;
import com.advertiser.service.mapper.PriceScheduleMapper;
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
 * REST controller for managing PriceSchedule.
 */
@RestController
@RequestMapping("/api")
public class PriceScheduleResource {

    private final Logger log = LoggerFactory.getLogger(PriceScheduleResource.class);
        
    @Inject
    private PriceScheduleRepository priceScheduleRepository;

    @Inject
    private PriceScheduleMapper priceScheduleMapper;

    /**
     * POST  /price-schedules : Create a new priceSchedule.
     *
     * @param priceScheduleDTO the priceScheduleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new priceScheduleDTO, or with status 400 (Bad Request) if the priceSchedule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/price-schedules",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceScheduleDTO> createPriceSchedule(@RequestBody PriceScheduleDTO priceScheduleDTO) throws URISyntaxException {
        log.debug("REST request to save PriceSchedule : {}", priceScheduleDTO);
        if (priceScheduleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("priceSchedule", "idexists", "A new priceSchedule cannot already have an ID")).body(null);
        }
        PriceSchedule priceSchedule = priceScheduleMapper.priceScheduleDTOToPriceSchedule(priceScheduleDTO);
        priceSchedule = priceScheduleRepository.save(priceSchedule);
        PriceScheduleDTO result = priceScheduleMapper.priceScheduleToPriceScheduleDTO(priceSchedule);
        return ResponseEntity.created(new URI("/api/price-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("priceSchedule", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /price-schedules : Updates an existing priceSchedule.
     *
     * @param priceScheduleDTO the priceScheduleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated priceScheduleDTO,
     * or with status 400 (Bad Request) if the priceScheduleDTO is not valid,
     * or with status 500 (Internal Server Error) if the priceScheduleDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/price-schedules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceScheduleDTO> updatePriceSchedule(@RequestBody PriceScheduleDTO priceScheduleDTO) throws URISyntaxException {
        log.debug("REST request to update PriceSchedule : {}", priceScheduleDTO);
        if (priceScheduleDTO.getId() == null) {
            return createPriceSchedule(priceScheduleDTO);
        }
        PriceSchedule priceSchedule = priceScheduleMapper.priceScheduleDTOToPriceSchedule(priceScheduleDTO);
        priceSchedule = priceScheduleRepository.save(priceSchedule);
        PriceScheduleDTO result = priceScheduleMapper.priceScheduleToPriceScheduleDTO(priceSchedule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("priceSchedule", priceScheduleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /price-schedules : get all the priceSchedules.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of priceSchedules in body
     */
    @RequestMapping(value = "/price-schedules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PriceScheduleDTO> getAllPriceSchedules() {
        log.debug("REST request to get all PriceSchedules");
        List<PriceSchedule> priceSchedules = priceScheduleRepository.findAll();
        return priceScheduleMapper.priceSchedulesToPriceScheduleDTOs(priceSchedules);
    }

    /**
     * GET  /price-schedules/:id : get the "id" priceSchedule.
     *
     * @param id the id of the priceScheduleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the priceScheduleDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/price-schedules/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceScheduleDTO> getPriceSchedule(@PathVariable Long id) {
        log.debug("REST request to get PriceSchedule : {}", id);
        PriceSchedule priceSchedule = priceScheduleRepository.findOne(id);
        PriceScheduleDTO priceScheduleDTO = priceScheduleMapper.priceScheduleToPriceScheduleDTO(priceSchedule);
        return Optional.ofNullable(priceScheduleDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /price-schedules/:id : delete the "id" priceSchedule.
     *
     * @param id the id of the priceScheduleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/price-schedules/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePriceSchedule(@PathVariable Long id) {
        log.debug("REST request to delete PriceSchedule : {}", id);
        priceScheduleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("priceSchedule", id.toString())).build();
    }

}

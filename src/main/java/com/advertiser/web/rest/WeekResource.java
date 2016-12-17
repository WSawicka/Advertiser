package com.advertiser.web.rest;

import com.advertiser.service.mapper.*;
import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.Week;

import com.advertiser.repository.WeekRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.WeekDTO;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing Week.
 */
@RestController
@RequestMapping("/api")
public class WeekResource {

    private final Logger log = LoggerFactory.getLogger(WeekResource.class);

    @Inject private WeekRepository weekRepository;
    @Inject private WeekMapper weekMapper;
    @Inject private DayMapper dayMapper;
    @Inject private HourMapper hourMapper;
    @Inject private SpotMapper spotMapper;
    @Inject private CampaignMapper campaignMapper;

    /**
     * POST  /weeks : Create a new week.
     *
     * @param weekDTO the weekDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weekDTO, or with status 400 (Bad Request) if the week has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weeks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekDTO> createWeek(@RequestBody WeekDTO weekDTO) throws URISyntaxException {
        log.debug("REST request to save Week : {}", weekDTO);
        if (weekDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("week", "idexists", "A new week cannot already have an ID")).body(null);
        }
        Week week = weekMapper.weekDTOToWeek(weekDTO);
        week = weekRepository.save(week);
        WeekDTO result = weekMapper.weekToWeekDTO(week);
        return ResponseEntity.created(new URI("/api/weeks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("week", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weeks : Updates an existing week.
     *
     * @param weekDTO the weekDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weekDTO,
     * or with status 400 (Bad Request) if the weekDTO is not valid,
     * or with status 500 (Internal Server Error) if the weekDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weeks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekDTO> updateWeek(@RequestBody WeekDTO weekDTO) throws URISyntaxException {
        log.debug("REST request to update Week : {}", weekDTO);
        if (weekDTO.getId() == null) {
            return createWeek(weekDTO);
        }
        Week week = weekMapper.weekDTOToWeek(weekDTO);
        week = weekRepository.save(week);
        WeekDTO result = weekMapper.weekToWeekDTO(week);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("week", weekDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weeks : get all the weeks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of weeks in body
     */
    @RequestMapping(value = "/weeks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WeekDTO> getAllWeeks() {
        log.debug("REST request to get all Weeks");
        List<Week> weeks = weekRepository.findAll();
        return weekMapper.weeksToWeekDTOs(weeks);
    }

    /**
     * GET  /weeks/:id : get the "id" week.
     *
     * @param id the id of the weekDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weekDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/weeks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeekDTO> getWeek(@PathVariable Long id) {
        log.debug("REST request to get Week : {}", id);
        Week week = weekRepository.findOne(id);
        WeekDTO weekDTO = weekMapper.weekToWeekDTO(week);
        return Optional.ofNullable(weekDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/year/{year}/weeks/{weekNumber}")
    public WeekDTO getWeekBy(@PathVariable Integer year, @PathVariable Integer weekNumber)
            throws IllegalAccessException {
        Week week = weekRepository.findByNumberAndYear(weekNumber, year);
        if (week == null) {
            week = weekRepository.save(new Week(weekNumber, year));
        }
        WeekDTO weekDTO = weekMapper.weekToWeekDTO(week);
        weekDTO.setDaysDTO(week.getDays(), dayMapper, hourMapper, spotMapper, campaignMapper);
        return weekDTO;
    }

    /**
     * DELETE  /weeks/:id : delete the "id" week.
     *
     * @param id the id of the weekDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/weeks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeek(@PathVariable Long id) {
        log.debug("REST request to delete Week : {}", id);
        weekRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("week", id.toString())).build();
    }

}

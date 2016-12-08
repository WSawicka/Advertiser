package com.advertiser.web.rest;

import com.advertiser.web.rest.errors.CustomParameterizedException;
import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.Day;

import com.advertiser.repository.DayRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.DayDTO;
import com.advertiser.service.mapper.DayMapper;
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
 * REST controller for managing Day.
 */
@RestController
@RequestMapping("/api")
public class DayResource {

    private final Logger log = LoggerFactory.getLogger(DayResource.class);

    @Inject
    private DayRepository dayRepository;

    @Inject
    private DayMapper dayMapper;

    /**
     * POST  /days : Create a new day.
     *
     * @param dayDTO the dayDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dayDTO, or with status 400 (Bad Request) if the day has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/days",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayDTO> createDay(@RequestBody DayDTO dayDTO) throws URISyntaxException {
        log.debug("REST request to save Day : {}", dayDTO);
        if (dayDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("day", "idexists", "A new day cannot already have an ID")).body(null);
        }
        Day day = dayMapper.dayDTOToDay(dayDTO);
        day = dayRepository.save(day);
        DayDTO result = dayMapper.dayToDayDTO(day);
        return ResponseEntity.created(new URI("/api/days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("day", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /days : Updates an existing day.
     *
     * @param dayDTO the dayDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dayDTO,
     * or with status 400 (Bad Request) if the dayDTO is not valid,
     * or with status 500 (Internal Server Error) if the dayDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/days",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayDTO> updateDay(@RequestBody DayDTO dayDTO) throws URISyntaxException {
        log.debug("REST request to update Day : {}", dayDTO);
        if (dayDTO.getId() == null) {
            return createDay(dayDTO);
        }
        Day day = dayMapper.dayDTOToDay(dayDTO);
        day = dayRepository.save(day);
        DayDTO result = dayMapper.dayToDayDTO(day);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("day", dayDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /days : get all the days.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of days in body
     */
    @RequestMapping(value = "/days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DayDTO> getAllDays() {
        log.debug("REST request to get all Days");
        List<Day> days = dayRepository.findAll();
        return dayMapper.daysToDayDTOs(days);
    }

    /**
     * GET  /days/:id : get the "id" day.
     *
     * @param id the id of the dayDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dayDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/days/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DayDTO> getDay(@PathVariable Long id) {
        log.debug("REST request to get Day : {}", id);
        Day day = dayRepository.findOne(id);
        DayDTO dayDTO = dayMapper.dayToDayDTO(day);
        return Optional.ofNullable(dayDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //Przerzucic do serwisu, pamietac o @Transactional
    /*public void whatever() {
        var itemsToUpdate = dayRepository.findByDatesBetween(dateFrom, dateTo).stream()
            .map(day -> hour) // to moze byc flat map, nie jestem pewien
            .filter(hour -> between(hourFrom, hourTo))
            .filter(hour -> hour.getSpots < 5)
            .collectToList();

        for(int i = 0; i < campaignSpotsLimit; i++) {
            if(itemsToUpdate.size() - 1 < i) {
                itemsToUpdate.get(i).getSpots.add(newSpot);
            } else {
                //jeżeli uzupełnił w mniejszej ilości godzin niż jest limit kampanii,
                //zwróć rozmiar listy itemsToUpdate i odpowiednie info
                //w przeciwnym razie pomyślnie wykonano task
            }
        }

        hoursRepo.saveAll(itemsToUpdate);

        //TODO ogarnąć CustomParameterizedException
        //TODO ogarnąć @ExceptionHandler

        return itemsUpdated;
    }*/

    /**
     * DELETE  /days/:id : delete the "id" day.
     *
     * @param id the id of the dayDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/days/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDay(@PathVariable Long id) {
        log.debug("REST request to delete Day : {}", id);
        dayRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("day", id.toString())).build();
    }

}

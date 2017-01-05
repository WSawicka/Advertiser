package com.advertiser.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.Business;

import com.advertiser.repository.BusinessRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.BusinessDTO;
import com.advertiser.service.mapper.BusinessMapper;
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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Business.
 */
@RestController
@RequestMapping("/api")
public class BusinessResource {

    private final Logger log = LoggerFactory.getLogger(BusinessResource.class);

    @Inject
    private BusinessRepository businessRepository;

    @Inject
    private BusinessMapper businessMapper;

    /**
     * POST  /businesses : Create a new business.
     *
     * @param businessDTO the businessDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new businessDTO, or with status 400 (Bad Request) if the business has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/businesses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessDTO> createBusiness(@RequestBody BusinessDTO businessDTO) throws URISyntaxException {
        log.debug("REST request to save Business : {}", businessDTO);
        if (businessDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("business", "idexists", "A new business cannot already have an ID")).body(null);
        }
        Business business = businessMapper.businessDTOToBusiness(businessDTO);
        business = businessRepository.save(business);
        BusinessDTO result = businessMapper.businessToBusinessDTO(business);
        return ResponseEntity.created(new URI("/api/businesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("business", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /businesses : Updates an existing business.
     *
     * @param businessDTO the businessDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated businessDTO,
     * or with status 400 (Bad Request) if the businessDTO is not valid,
     * or with status 500 (Internal Server Error) if the businessDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/businesses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessDTO> updateBusiness(@RequestBody BusinessDTO businessDTO) throws URISyntaxException {
        log.debug("REST request to update Business : {}", businessDTO);
        if (businessDTO.getId() == null) {
            return createBusiness(businessDTO);
        }
        Business business = businessMapper.businessDTOToBusiness(businessDTO);
        business = businessRepository.save(business);
        BusinessDTO result = businessMapper.businessToBusinessDTO(business);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("business", businessDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /businesses : get all the businesses.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of businesses in body
     */
    @RequestMapping(value = "/businesses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BusinessDTO> getAllBusinesses(@RequestParam(required = false) String filter) {
        log.debug("REST request to get all Businesses");
        List<Business> businesses = businessRepository.findAll();
        return businessMapper.businessesToBusinessDTOs(businesses);
    }

    /**
     * GET  /businesses/:id : get the "id" business.
     *
     * @param id the id of the businessDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the businessDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/businesses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessDTO> getBusiness(@PathVariable Long id) {
        log.debug("REST request to get Business : {}", id);
        Business business = businessRepository.findOne(id);
        BusinessDTO businessDTO = businessMapper.businessToBusinessDTO(business);
        return Optional.ofNullable(businessDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /businesses/:id : delete the "id" business.
     *
     * @param id the id of the businessDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/businesses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id) {
        log.debug("REST request to delete Business : {}", id);
        businessRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("business", id.toString())).build();
    }

}

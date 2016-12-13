package com.advertiser.web.rest;

import com.advertiser.domain.Campaign;
import com.codahale.metrics.annotation.Timed;
import com.advertiser.domain.SpotInfo;

import com.advertiser.repository.SpotInfoRepository;
import com.advertiser.web.rest.util.HeaderUtil;
import com.advertiser.service.dto.SpotInfoDTO;
import com.advertiser.service.mapper.SpotInfoMapper;
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
 * REST controller for managing SpotInfo.
 */
@RestController
@RequestMapping("/api")
public class SpotInfoResource {

    private final Logger log = LoggerFactory.getLogger(SpotInfoResource.class);

    @Inject
    private SpotInfoRepository spotInfoRepository;

    @Inject
    private SpotInfoMapper spotInfoMapper;

    /**
     * POST  /spot-infos : Create a new spotInfo.
     *
     * @param spotInfoDTO the spotInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spotInfoDTO, or with status 400 (Bad Request) if the spotInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/spot-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpotInfoDTO> createSpotInfo(@RequestBody SpotInfoDTO spotInfoDTO) throws URISyntaxException {
        log.debug("REST request to save SpotInfo : {}", spotInfoDTO);
        if (spotInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("spotInfo", "idexists", "A new spotInfo cannot already have an ID")).body(null);
        }
        SpotInfo spotInfo = spotInfoMapper.spotInfoDTOToSpotInfo(spotInfoDTO);
        spotInfo = spotInfoRepository.save(spotInfo);
        SpotInfoDTO result = spotInfoMapper.spotInfoToSpotInfoDTO(spotInfo);
        return ResponseEntity.created(new URI("/api/spot-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("spotInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spot-infos : Updates an existing spotInfo.
     *
     * @param spotInfoDTO the spotInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spotInfoDTO,
     * or with status 400 (Bad Request) if the spotInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the spotInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/spot-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpotInfoDTO> updateSpotInfo(@RequestBody SpotInfoDTO spotInfoDTO) throws URISyntaxException {
        log.debug("REST request to update SpotInfo : {}", spotInfoDTO);
        if (spotInfoDTO.getId() == null) {
            return createSpotInfo(spotInfoDTO);
        }
        SpotInfo spotInfo = spotInfoMapper.spotInfoDTOToSpotInfo(spotInfoDTO);
        spotInfo = spotInfoRepository.save(spotInfo);
        SpotInfoDTO result = spotInfoMapper.spotInfoToSpotInfoDTO(spotInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("spotInfo", spotInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spot-infos : get all the spotInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of spotInfos in body
     */
    @RequestMapping(value = "/spot-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SpotInfoDTO> getAllSpotInfos() {
        log.debug("REST request to get all SpotInfos");
        List<SpotInfo> spotInfos = spotInfoRepository.findAll();
        return spotInfoMapper.spotInfosToSpotInfoDTOs(spotInfos);
    }

    @GetMapping("/spot-infos/campaignId/{campaignId}")
    @Timed
    public List<SpotInfoDTO> getSpotInfosIn(@PathVariable Long campaignId){
        List<SpotInfo> spotInfos = spotInfoRepository.findAllByCampaignId(campaignId);
        return spotInfoMapper.spotInfosToSpotInfoDTOs(spotInfos);
    }

    /**
     * GET  /spot-infos/:id : get the "id" spotInfo.
     *
     * @param id the id of the spotInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spotInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/spot-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpotInfoDTO> getSpotInfo(@PathVariable Long id) {
        log.debug("REST request to get SpotInfo : {}", id);
        SpotInfo spotInfo = spotInfoRepository.findOne(id);
        SpotInfoDTO spotInfoDTO = spotInfoMapper.spotInfoToSpotInfoDTO(spotInfo);
        return Optional.ofNullable(spotInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /spot-infos/:id : delete the "id" spotInfo.
     *
     * @param id the id of the spotInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/spot-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpotInfo(@PathVariable Long id) {
        log.debug("REST request to delete SpotInfo : {}", id);
        spotInfoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("spotInfo", id.toString())).build();
    }

}

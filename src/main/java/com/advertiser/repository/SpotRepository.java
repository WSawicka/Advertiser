package com.advertiser.repository;

import com.advertiser.domain.Spot;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Spot entity.
 */
@SuppressWarnings("unused")
public interface SpotRepository extends JpaRepository<Spot,Long> {

    List<Spot> findAllSpotsByHourId(Long hourId);

    Long countByCampaignId(Long id);

    List<Spot> findAllByCampaignIdOrderByDateTime(Long campaignId);

    @Query("select count(s) from Spot s where s.dateTime between :startDate and :endDate")
    Integer findAllSpotsBetween(@Param("startDate") ZonedDateTime start, @Param("endDate") ZonedDateTime end);

}

package com.advertiser.repository;

import com.advertiser.domain.Campaign;

import com.advertiser.domain.enumeration.CampaignState;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Campaign entity.
 */
@SuppressWarnings("unused")
public interface CampaignRepository extends JpaRepository<Campaign,Long> {

    @Query("select distinct campaign from Campaign campaign left join fetch campaign.priceSchedules")
    List<Campaign> findAllWithEagerRelationships();

    @Query("select campaign from Campaign campaign left join fetch campaign.priceSchedules " +
        "where campaign.id =:id")
    Campaign findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select c from Campaign c where c.endDate > :dateTime and (select count(s) from Spot s where s.campaign = c) < c.spotAmount")
    List<Campaign> findAllAvailableCampaigns(@Param("dateTime") ZonedDateTime dateTime);

    @Query("select distinct c from Campaign c where c.userId = :userId")
    List<Campaign> findAllWithEagerRelationshipsOfUser(@Param("userId") Long userId);

    @Query("select c from Campaign c where (c.startDate between :startDate and :endDate) or (c.endDate between :startDate and :endDate)")
    List<Campaign> findAllCampaignsBetween(@Param("startDate") ZonedDateTime start, @Param("endDate") ZonedDateTime end);

    @Query("select c from Campaign c where c.campaignState = :started or c.campaignState = :before")
    List<Campaign> findAllOfState(@Param("started") CampaignState started, @Param("before") CampaignState before);

    @Query("select campaign from Campaign campaign left join fetch campaign.priceSchedules where campaign.name = :name and campaign.startDate = :startDate")
    Campaign findOneWithNameAndStartDate(@Param("name") String name, @Param("startDate") ZonedDateTime startDate);
}

package com.advertiser.repository;

import com.advertiser.domain.Campaign;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

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

    /*
    * select * from campaign c where c.spot_amount <
    *       (select count(*) from spot s where s.campaignId = c.id)
    *    and c.endDate > NOW();
    *
    @Query("select c from Campaign c where c.spotAmount < " +
        "(select count(s) from Spot s where s.campaign = c) and c.endDate > :dateTime")
    * */
    @Query("select c from Campaign c where c.endDate > :dateTime and (select count(s) from Spot s where s.campaign = c) < c.spotAmount")
    List<Campaign> findAllAvailableCampaigns(@Param("dateTime") ZonedDateTime dateTime);
}

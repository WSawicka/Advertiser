package com.advertiser.repository;

import com.advertiser.domain.Campaign;
import com.advertiser.domain.SpotInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SpotInfo entity.
 */
@SuppressWarnings("unused")
public interface SpotInfoRepository extends JpaRepository<SpotInfo,Long> {
    List<SpotInfo> findAllByCampaignId(Long campaignId);
}

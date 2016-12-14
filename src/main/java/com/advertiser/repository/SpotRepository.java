package com.advertiser.repository;

import com.advertiser.domain.Spot;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Spot entity.
 */
@SuppressWarnings("unused")
public interface SpotRepository extends JpaRepository<Spot,Long> {

    List<Spot> findAllSpotsByHourId(Long hourId);
}

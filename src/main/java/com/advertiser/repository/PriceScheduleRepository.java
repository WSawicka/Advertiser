package com.advertiser.repository;

import com.advertiser.domain.PriceSchedule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PriceSchedule entity.
 */
@SuppressWarnings("unused")
public interface PriceScheduleRepository extends JpaRepository<PriceSchedule,Long> {

}

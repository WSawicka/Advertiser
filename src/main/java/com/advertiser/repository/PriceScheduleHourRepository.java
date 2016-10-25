package com.advertiser.repository;

import com.advertiser.domain.PriceScheduleHour;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PriceScheduleHour entity.
 */
@SuppressWarnings("unused")
public interface PriceScheduleHourRepository extends JpaRepository<PriceScheduleHour,Long> {

}

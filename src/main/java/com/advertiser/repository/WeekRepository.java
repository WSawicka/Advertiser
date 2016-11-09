package com.advertiser.repository;

import com.advertiser.domain.Week;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Week entity.
 */
@SuppressWarnings("unused")
public interface WeekRepository extends JpaRepository<Week,Long> {

    Week findByNumberAndYear(Integer weekNumber, Integer year);
}

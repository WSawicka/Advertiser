package com.advertiser.repository;

import com.advertiser.domain.Day;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Day entity.
 */
@SuppressWarnings("unused")
public interface DayRepository extends JpaRepository<Day,Long> {

    @Query("select d from Day d where d.dateTime >= :startDate and d.dateTime <= :endDate order by d.dateTime")
    List<Day> findAllBetween(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);
}

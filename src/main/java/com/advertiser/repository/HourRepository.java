package com.advertiser.repository;

import com.advertiser.domain.Hour;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Hour entity.
 */
@SuppressWarnings("unused")
public interface HourRepository extends JpaRepository<Hour,Long> {
/*select count(h.id) from hour h
    where h.day_id in (select d.id from day d where d.number between startDate and endDate)
        and (select count(s.id) from spot s where s.hour_id = h.id) < 5;
*/
    @Query(value = "select count(h.id) from hour h where h.day_id in (select d.id from day d where (d.date_time between :startDate and :endDate)) and (select count(s.id) from spot s where s.hour_id = h.id) < 5", nativeQuery = true)
    Integer findAllAvailableHours(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "select count(h.id) from hour h where h.day_id in (select d.id from day d where (d.date_time between :startDate and :endDate)) and (select count(s.id) from spot s where s.hour_id = h.id) < 5 and h.number in (:hoursPreferred)", nativeQuery = true)
    Integer findAllAvailablePreferredHours(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("hoursPreferred") List<Integer> hoursPreferred);

    //@Query(value = "select d.id from day d where (d.date_time between :startDate and :endDate)", nativeQuery = true)
    //@Query(value = "select count(h.id) from hour h where (select count(s.id) from spot s where s.hour_id = h.id) < 5", nativeQuery = true)
}

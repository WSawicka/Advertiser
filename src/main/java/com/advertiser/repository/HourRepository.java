package com.advertiser.repository;

import com.advertiser.domain.Hour;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hour entity.
 */
@SuppressWarnings("unused")
public interface HourRepository extends JpaRepository<Hour,Long> {

}

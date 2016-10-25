package com.advertiser.repository;

import com.advertiser.domain.Report;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Report entity.
 */
@SuppressWarnings("unused")
public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query("select distinct report from Report report left join fetch report.campaigns")
    List<Report> findAllWithEagerRelationships();

    @Query("select report from Report report left join fetch report.campaigns where report.id =:id")
    Report findOneWithEagerRelationships(@Param("id") Long id);

}

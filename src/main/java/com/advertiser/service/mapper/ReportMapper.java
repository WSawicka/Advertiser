package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.ReportDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Report and its DTO ReportDTO.
 */
@Mapper(componentModel = "spring", uses = {CampaignMapper.class, })
public interface ReportMapper {

    ReportDTO reportToReportDTO(Report report);

    List<ReportDTO> reportsToReportDTOs(List<Report> reports);

    Report reportDTOToReport(ReportDTO reportDTO);

    List<Report> reportDTOsToReports(List<ReportDTO> reportDTOs);

    default Campaign campaignFromId(Long id) {
        if (id == null) {
            return null;
        }
        Campaign campaign = new Campaign();
        campaign.setId(id);
        return campaign;
    }
}

package com.advertiser.service;

import com.advertiser.domain.Campaign;
import com.advertiser.domain.PriceSchedule;
import com.advertiser.domain.PriceScheduleHour;
import com.advertiser.domain.Spot;
import com.advertiser.repository.CampaignRepository;
import com.advertiser.repository.SpotRepository;
import com.advertiser.service.dto.CampaignDTO;
import com.advertiser.service.mapper.CampaignMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ws on 1/6/17.
 */
@Service
@Transactional(readOnly = true)
public class ReportService {

    @Setter
    @Getter
    private class Report{
        private Integer campaignsAmount = 0;
        private Integer campaignsStarted = 0;
        private Integer campaignsCompleted = 0;
        private Integer spotsAmount = 0;
        private Integer spotsPerCampaign = 0;
        private Double priceAll = 0.0;
    }

    @Setter
    @Getter
    private class CampaignReport{
        private CampaignDTO campaign;
        private Long amount = 0L;
        private Double price = 0.0;
    }

    @Inject private CampaignRepository campaignRepository;
    @Inject private CampaignMapper campaignMapper;
    @Inject private SpotRepository spotRepository;

    private Report report;

    public Report getReportOf(Integer year){
        this.report = new Report();
        ZonedDateTime start = ZonedDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
        ZonedDateTime end = start.plusYears(1).minusDays(1);

        List<Campaign> campaigns = campaignRepository.findAllCampaignsBetween(start, end);
        this.report.setCampaignsAmount(campaigns.size());
        for(Campaign campaign : campaigns){
            ZonedDateTime csd = campaign.getStartDate();
            ZonedDateTime ced = campaign.getEndDate();
            if((csd.isAfter(start) || csd.isEqual(start)) && (csd.isBefore(end) || csd.isEqual(end)))
                this.report.campaignsStarted++;
            if((ced.isAfter(start) || ced.isEqual(start)) && (ced.isBefore(end) || ced.isEqual(end)))
                this.report.campaignsCompleted++;
        }
        this.report.setSpotsAmount(spotRepository.findAllSpotsBetween(start, end));
        this.report.setSpotsPerCampaign(this.report.getSpotsAmount() / this.report.getCampaignsAmount());

        for(Campaign campaign : campaigns){
            List<Spot> spots = spotRepository.findAllByCampaignIdOrderByDateTime(campaign.getId());
            for(Spot spot : spots){
                BigDecimal price = getPriceOf(spot.getDateTime(), campaign);
                this.report.priceAll += price.doubleValue();
            }
        }

        return report;
    }

    public List<CampaignReport> getCampaignsWithAmountsAndPrices(ZonedDateTime start, ZonedDateTime end){
        List<CampaignReport> result = new ArrayList<>();
        List<Campaign> campaigns = campaignRepository.findAllCampaignsBetween(start, end);
        for(Campaign campaign : campaigns){
            CampaignReport cr = new CampaignReport();
            cr.setCampaign(campaignMapper.campaignToCampaignDTO(campaign));
            cr.setAmount(spotRepository.countByCampaignId(campaign.getId()));

            Double priceCampaign = 0.0;
            List<Spot> spots = spotRepository.findAllByCampaignIdOrderByDateTime(campaign.getId());
            for(Spot spot : spots){
                BigDecimal price = getPriceOf(spot.getDateTime(), campaign);
                 priceCampaign += price.doubleValue();
            }
            cr.setPrice(priceCampaign);
            result.add(cr);
        }
        return result;
    }

    private BigDecimal getPriceOf(ZonedDateTime dateTime, Campaign campaign){
        for(PriceSchedule ps : campaign.getPriceSchedules()){
            if(dateTime.isAfter(ps.getStartDate()) && dateTime.isBefore(ps.getEndDate())){
                List<PriceScheduleHour> pshList = new ArrayList<>();
                pshList.addAll(ps.getPriceScheduleHours());

                int hour = dateTime.getHour();

                for(PriceScheduleHour psh : pshList){
                    if(psh.getHour() == hour)
                        return psh.getPrice();
                }
            }
        }
        return BigDecimal.valueOf(0.0); //no schedule - no price
    }
}

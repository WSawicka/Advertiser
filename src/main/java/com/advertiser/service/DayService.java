package com.advertiser.service;

import com.advertiser.domain.*;
import com.advertiser.repository.DayRepository;
import com.advertiser.repository.HourRepository;
import com.advertiser.repository.SpotInfoRepository;
import com.advertiser.service.dto.SpotDTO;
import com.advertiser.service.mapper.CampaignMapper;
import com.advertiser.web.rest.SpotResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by ws on 12/19/16.
 */
@Service
@Transactional(readOnly = true)
public class DayService {

    private Random rand = new Random();
    private int[] starts = new int[] {0, 6, 12, 18};

    @Inject DayRepository dayRepository;

    @Inject HourRepository hourRepository;

    @Inject SpotResource spotResource;

    @Inject CampaignMapper campaignMapper;

    //randomize hours with range from 0 to 23
    public List<SpotDTO> generateSpotsFor(Campaign campaign, Long toGenerate, Long spotInfoId, Integer spotsLimit,
                                    List<Integer> hoursPreferred) {
        List<Day> days = dayRepository.findAllBetween(campaign.getStartDate(), campaign.getEndDate());
        int[] daysVisited = new int[days.size()];
        List<Long> hoursVisited = new ArrayList<>();
        List<SpotDTO> spotsToGenerate = new ArrayList<>();

        while(toGenerate > 0) {
            Day day = (days.size() > 1) ? days.get(rand.nextInt(days.size()-1)) : days.get(0);
            Set<Hour> hours = day.getHours();
            int numberRand = randGaussian(0, 23);
            Hour hour = getHourWith(numberRand, hours);
            //if is proper hour
            if (hour != null && hoursPreferred.contains(numberRand)) {
                //if it is available to create spot
                if (hour.getSpots().size() < 5 && !hasSpotFromCampaign(hour, campaign) && toGenerate > 0) {
                    //to prevent too much insert in a day/hour
                    if(!hoursVisited.contains(hour.getId()) && daysVisited[days.indexOf(day)] < spotsLimit){
                        SpotDTO newSpot = new SpotDTO();
                        newSpot.setDateTime(day.getDateTime().withHour(hour.getNumber()));
                        newSpot.setHourId(hour.getId());
                        newSpot.setCampaignId(campaign.getId());
                        newSpot.setCampaignDTO(campaignMapper.campaignToCampaignDTO(campaign));
                        newSpot.setSpotInfoId(spotInfoId);
                        newSpot.setSpotName(campaign.getNameShort());
                        newSpot.setSpotNumber(hour.getSpots().size() + 1);
                        spotsToGenerate.add(newSpot);

                        toGenerate--;
                        daysVisited[days.indexOf(day)]++;
                    }
                }
                hoursVisited.add(hour.getId());
            }
            hours.remove(hour);
            //TODO: zapezpieczyć przed czekaniem na wylosowanie wartości brzegowych
        }
        return spotsToGenerate;
    }

    private Hour getHourWith(int number, Set<Hour> hours){
        for(Hour h : hours){
            if (h.getNumber() == number)
                return h;
        }
        return null;
    }

    private boolean hasSpotFromCampaign(Hour hour, Campaign campaign){
        for (Spot spot : hour.getSpots()) {
            if (spot.getCampaign().getId().equals(campaign.getId())) return true;
        }
        return false;
    }

    /* Function returns pseudorandom, Gaussian ("normally") distributed integer value, where x1 and xn are the two
    * edges of function, from which are set mean and deviation of gauss function (public double nextGaussian()) */
    private int randGaussian(int x1, int xn){
        double deviation = (x1 + xn)/2;
        double mean = x1 + deviation;

        double result = rand.nextGaussian() * mean + deviation;
        return (int) Math.round(result);
    }
}


// próby jakości działania randomów
        /*int[] tab = new int[24];
        for(int i=0; i<500; i++){
            int value = randGaussian(0, 23);
            if (value>=0 && value<=23) tab[value]++;
        }*/

package com.advertiser.service;

import com.advertiser.domain.*;
import com.advertiser.repository.DayRepository;
import com.advertiser.repository.HourRepository;
import com.advertiser.repository.SpotInfoRepository;
import com.advertiser.service.dto.CampaignDTO;
import com.advertiser.service.dto.SpotDTO;
import com.advertiser.service.mapper.CampaignMapper;
import com.advertiser.web.rest.SpotResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by ws on 12/19/16.
 */
@Service
@Transactional(readOnly = true)
public class DayService {

    private Random rand = new Random();
    private int[] starts = new int[] {0, 6, 12, 18};
    private List<Integer> hours = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);

    @Inject DayRepository dayRepository;

    @Inject HourRepository hourRepository;

    @Inject SpotResource spotResource;

    @Inject CampaignMapper campaignMapper;

    //randomize hours with range from 0 to 23
    public List<SpotDTO> generateSpotsFor(Campaign campaign, List<Integer> toGenerate, Long spotInfoId,
                                          Integer spotsLimit, List<Integer> hoursPreferred) {
        int inHoursPreferred = toGenerate.get(0);
        int inOtherHours = toGenerate.get(1);

        if(inOtherHours == 0) {
            return generateRandomizedSpots(campaign, inHoursPreferred, spotInfoId, spotsLimit, hoursPreferred);
        } else {
            return allPreferredAndOtherHours(campaign, inHoursPreferred, inOtherHours, hoursPreferred,
                spotInfoId, spotsLimit);
        }
    }

    private List<SpotDTO> generateRandomizedSpots(Campaign campaign, Integer toGenerate,
                                           Long spotInfoId, Integer spotsLimit, List<Integer> hoursPreferred){
        List<Day> days = dayRepository.findAllBetween(campaign.getStartDate(), campaign.getEndDate());
        int[] daysVisited = new int[days.size()];
        List<SpotDTO> spotsToGenerate = new ArrayList<>();

        while(toGenerate > 0) {
            Day day = (days.size() > 1) ? days.get(rand.nextInt(days.size()-1)) : days.get(0);
            Hour hour = getHourWith(randGaussian(0, 23), day.getHours());
            if (hour != null) {
                if (checkIfDayIsVisitedEnough(daysVisited, days.indexOf(day), spotsLimit)) {
                    if (checkIfHourIsAvailable(hour, hoursPreferred, campaign) && toGenerate > 0) {
                        //add to result list and set values (add visit in day and less amount of spots to generate)
                        SpotDTO newSpot = new SpotDTO(day.getDateTime().withHour(hour.getNumber()),
                            hour.getId(), campaignMapper.campaignToCampaignDTO(campaign), spotInfoId,
                            hour.getSpots().size() + 1);
                        spotsToGenerate.add(newSpot);
                        toGenerate--;
                        daysVisited[days.indexOf(day)]++;
                    }
                    day.getHours().remove(hour);
                    if (day.getHours().size() == 0)
                        days.remove(day);
                } else
                    days.remove(day);
            }
        }
        return spotsToGenerate;
    }

    private List<SpotDTO> allPreferredAndOtherHours(Campaign campaign, int inPreferred, int inOther,
                                                    List<Integer> hoursPreferred, Long spotInfoId, Integer spotsLimit){
        List<SpotDTO> spotsToGenerate = new ArrayList<>();
        List<Day> days = dayRepository.findAllBetween(campaign.getStartDate(), campaign.getEndDate());

        int preferred = inPreferred;
        while(preferred != 0) {
            for (Day day : days) {
                Set<Hour> hours = day.getHours();
                for(Hour hour : hours) {
                    if(checkIfHourIsAvailable(hour, hoursPreferred, campaign) && preferred > 0) {
                        SpotDTO newSpot = new SpotDTO(day.getDateTime().withHour(hour.getNumber()),
                            hour.getId(), campaignMapper.campaignToCampaignDTO(campaign), spotInfoId,
                            hour.getSpots().size() + 1);
                        spotsToGenerate.add(newSpot);
                        preferred--;
                    }
                }
            }
        }

        Integer other = inOther;
        int otherSpotsLimit = spotsLimit - hoursPreferred.size();
        int[] daysVisited = new int[days.size()];
        List<Integer> otherHours = new ArrayList<>();
        otherHours.addAll(hours);
        otherHours.removeAll(hoursPreferred);
        List<SpotDTO> otherSpots = generateRandomizedSpots(campaign, other, spotInfoId, otherSpotsLimit, otherHours);
        spotsToGenerate.addAll(otherSpots);
        return spotsToGenerate;
    }

    private boolean checkIfHourIsAvailable(Hour hour, List<Integer> availableHours, Campaign campaign){
        if (availableHours.contains(hour.getNumber())) {
            if (hour.getSpots().size() < 5 && !hasSpotFromCampaign(hour, campaign)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfDayIsVisitedEnough(int[] daysVisited, int dayIndex, Integer spotsLimit) {
        return  daysVisited[dayIndex] <= spotsLimit;
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

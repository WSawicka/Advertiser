package com.advertiser.service.dto;

import com.advertiser.domain.Day;
import com.advertiser.domain.enumeration.DayName;
import com.advertiser.service.mapper.CampaignMapper;
import com.advertiser.service.mapper.DayMapper;
import com.advertiser.service.mapper.HourMapper;
import com.advertiser.service.mapper.SpotMapper;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;


/**
 * A DTO for the Week entity.
 */
public class WeekDTO implements Serializable {

    private Long id;
    private Integer number;
    private Integer year;
    private Set<DayDTO> days = new HashSet<>();

    public WeekDTO(){}

    public WeekDTO(Integer weekNumber, Integer year) {
        this.number = weekNumber;
        this.year = year;

        for(int i=1; i<=7; i++) {
            DateTime dateTime = new DateTime().withYear(year).withWeekOfWeekyear(weekNumber).withDayOfWeek(i);
            String dayName = dateTime.dayOfWeek().getAsText().toUpperCase();
            int dayNum = dateTime.getDayOfMonth();
            DayDTO day = new DayDTO(DayName.valueOf(dayName), dayNum);
            days.add(day);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<DayDTO> getDays() {
        return days;
    }

    public void setDays(Set<DayDTO> days) {
        this.days = days;
    }

    public void setDaysDTO(Set<Day> days, DayMapper dayMapper, HourMapper hourMapper,
                           SpotMapper spotMapper, CampaignMapper campaignMapper){
        for(Day day : days){
            DayDTO dayDTO = dayMapper.dayToDayDTO(day);
            List hours = new ArrayList();
            hours.addAll(day.getHours());
            dayDTO.setHoursDTO(new HashSet<>(hours), hourMapper, spotMapper, campaignMapper);
            this.days.add(dayDTO);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WeekDTO weekDTO = (WeekDTO) o;

        if ( ! Objects.equals(id, weekDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WeekDTO{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", year='" + year + "'" +
            '}';
    }
}

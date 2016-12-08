package com.advertiser.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

import com.advertiser.domain.Hour;
import com.advertiser.domain.enumeration.DayName;
import com.advertiser.service.mapper.CampaignMapper;
import com.advertiser.service.mapper.HourMapper;
import com.advertiser.service.mapper.SpotMapper;

/**
 * A DTO for the Day entity.
 */
public class DayDTO implements Serializable {

    private Long id;
    private Integer number;
    private ZonedDateTime dateTime;
    private DayName dayName;
    private Set<HourDTO> hours = new HashSet<>();

    public DayDTO(){}

    public DayDTO (DayName name, Integer number) {
        this.dayName = name;
        this.number = number;

        for (int i = 0; i < 24; i++) {
            HourDTO hour = new HourDTO(i);
            hours.add(hour);
        }
    }

    private Long weekId;

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

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DayName getDayName() {
        return dayName;
    }

    public void setDayName(DayName dayName) {
        this.dayName = dayName;
    }

    public Long getWeekId() {
        return weekId;
    }

    public void setWeekId(Long weekId) {
        this.weekId = weekId;
    }

    public Set<HourDTO> getHours() {
        return hours;
    }

    public void setHours(Set<HourDTO> hours) {
        this.hours = hours;
    }

    public void setHoursDTO(Set<Hour> hours, HourMapper hourMapper, SpotMapper spotMapper,
                            CampaignMapper campaignMapper) {
        for(Hour hour : hours){
            HourDTO hourDTO = hourMapper.hourToHourDTO(hour);
            List spots = new ArrayList();
            spots.addAll(hour.getSpots());
            hourDTO.setSpotsDTO(new HashSet<>(spots), spotMapper, campaignMapper);
            this.hours.add(hourDTO);
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

        DayDTO dayDTO = (DayDTO) o;

        if ( ! Objects.equals(id, dayDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DayDTO{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", dayName='" + dayName + "'" +
            '}';
    }
}

package com.advertiser.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.advertiser.domain.enumeration.DayName;

/**
 * A DTO for the Day entity.
 */
public class DayDTO implements Serializable {

    private Long id;

    private Integer number;

    private DayName dayName;


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

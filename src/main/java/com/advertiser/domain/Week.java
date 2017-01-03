package com.advertiser.domain;

import com.advertiser.domain.enumeration.DayName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;

/**
 * not an ignored comment
 *
 */
@ApiModel(description = ""
    + "not an ignored comment                                                 "
    + "")
@Entity
@Table(name = "week")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Week implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "year")
    private Integer year;

    @OneToMany(mappedBy = "week", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Day> days = new HashSet<>();

    public Week() {}

    public Week(Integer weekNumber, Integer year) {
        this.number = weekNumber;
        this.year = year;

        for(int i=1; i<=7; i++) {
            DateTime dateTime = new DateTime().withWeekyear(year).withWeekOfWeekyear(weekNumber).withDayOfWeek(i).withTime(0, 0, 0, 0);
            String dayName = dateTime.dayOfWeek().getAsText().toUpperCase();
            int dayNum = dateTime.getDayOfMonth();
            Day day = new Day(DayName.valueOf(dayName), dayNum, this, dateTime);
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

    public Week number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getYear() {
        return year;
    }

    public Week year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<Day> getDays() {
        return days;
    }

    public Week days(Set<Day> days) {
        this.days = days;
        return this;
    }

    public Week addDay(Day day) {
        days.add(day);
        day.setWeek(this);
        return this;
    }

    public Week removeDay(Day day) {
        days.remove(day);
        day.setWeek(null);
        return this;
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Week week = (Week) o;

        if (!id.equals(week.id)) return false;
        if (!number.equals(week.number)) return false;
        return year.equals(week.year);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Week{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", year='" + year + "'" +
            '}';
    }
}

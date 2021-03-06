package com.advertiser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.advertiser.domain.enumeration.DayName;
import org.hibernate.annotations.Cache;
import org.joda.time.DateTime;

/**
 * A Day.
 */
@Entity
@BatchSize(size = 7)
@Table(name = "day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Day implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_name")
    private DayName dayName;

    @BatchSize(size = 7)
    @OneToMany(mappedBy = "day", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Hour> hours = new HashSet<>();

    @ManyToOne
    private Week week;

    public Day(){}

    public Day(DayName name, Integer number, Week week, DateTime dt){
        this.dayName = name;
        this.number = number;
        this.week = week;

        ZonedDateTime zdt = ZonedDateTime.of(dt.getYear(),
            dt.getMonthOfYear(),
            dt.getDayOfMonth(),
            dt.getHourOfDay(),
            dt.getMinuteOfHour(),
            dt.getSecondOfMinute(),
            dt.getMillisOfSecond() * 1_000_000,
            ZoneId.of(dt.getZone().getID(), ZoneId.SHORT_IDS));
        this.dateTime = zdt;

        for (int i=0; i<24; i++){
            Hour hour = new Hour(i, this);
            hours.add(hour);
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

    public Day number(Integer number) {
        this.number = number;
        return this;
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

    public Day dayName(DayName dayName) {
        this.dayName = dayName;
        return this;
    }

    public void setDayName(DayName dayName) {
        this.dayName = dayName;
    }

    public Set<Hour> getHours() {
        return hours;
    }

    public Day hours(Set<Hour> hours) {
        this.hours = hours;
        return this;
    }

    public Day addHour(Hour hour) {
        hours.add(hour);
        hour.setDay(this);
        return this;
    }

    public Day removeHour(Hour hour) {
        hours.remove(hour);
        hour.setDay(null);
        return this;
    }

    public void setHours(Set<Hour> hours) {
        this.hours = hours;
    }

    public Week getWeek() {
        return week;
    }

    public Day week(Week week) {
        this.week = week;
        return this;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        if (day.id != null && !id.equals(day.id)) return false;
        if (!number.equals(day.number)) return false;
        if (dayName != day.dayName) return false;
        return week.equals(day.week);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Day{" +
            "id=" + id +
            ", number='" + number + "'" +
            ", dayName='" + dayName + "'" +
            '}';
    }
}

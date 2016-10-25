package com.advertiser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.advertiser.domain.enumeration.DayName;

/**
 * A Day.
 */
@Entity
@Table(name = "day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Day implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_name")
    private DayName dayName;

    @OneToMany(mappedBy = "day")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Hour> hours = new HashSet<>();

    @ManyToOne
    private Week week;

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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Day day = (Day) o;
        if(day.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, day.id);
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

package com.advertiser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Hour.
 */
@Entity
@Table(name = "hour")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @OneToMany(mappedBy = "hour")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Spot> spots = new HashSet<>();

    @ManyToOne
    private Day day;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public Hour number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Set<Spot> getSpots() {
        return spots;
    }

    public Hour spots(Set<Spot> spots) {
        this.spots = spots;
        return this;
    }

    public Hour addSpot(Spot spot) {
        spots.add(spot);
        spot.setHour(this);
        return this;
    }

    public Hour removeSpot(Spot spot) {
        spots.remove(spot);
        spot.setHour(null);
        return this;
    }

    public void setSpots(Set<Spot> spots) {
        this.spots = spots;
    }

    public Day getDay() {
        return day;
    }

    public Hour day(Day day) {
        this.day = day;
        return this;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hour hour = (Hour) o;
        if(hour.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hour.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hour{" +
            "id=" + id +
            ", number='" + number + "'" +
            '}';
    }
}

package com.advertiser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Spot.
 */
@Entity
@Table(name = "spot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Spot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @ManyToOne
    @JsonIgnore
    private Hour hour;

    @ManyToOne
    @JsonIgnore
    private Campaign campaign;

    @ManyToOne
    private SpotInfo spotInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public Spot dateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Hour getHour() {
        return hour;
    }

    public Spot hour(Hour hour) {
        this.hour = hour;
        return this;
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public Spot campaign(Campaign campaign) {
        this.campaign = campaign;
        return this;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public SpotInfo getSpotInfo() {
        return spotInfo;
    }

    public Spot spotInfo(SpotInfo spotInfo) {
        this.spotInfo = spotInfo;
        return this;
    }

    public void setSpotInfo(SpotInfo spotInfo) {
        this.spotInfo = spotInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Spot spot = (Spot) o;

        if (!id.equals(spot.id)) return false;
        if (!dateTime.equals(spot.dateTime)) return false;
        if (!hour.equals(spot.hour)) return false;
        if (!campaign.equals(spot.campaign)) return false;
        return spotInfo.equals(spot.spotInfo);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Spot{" +
            "id=" + id +
            ", dateTime='" + dateTime + "'" +
            '}';
    }
}

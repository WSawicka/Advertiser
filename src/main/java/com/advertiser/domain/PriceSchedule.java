package com.advertiser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PriceSchedule.
 */
@Entity
@Table(name = "price_schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PriceSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @OneToMany(mappedBy = "priceSchedule")
    @JsonIgnore
    private Set<PriceScheduleHour> priceScheduleHours = new HashSet<>();

    @ManyToMany(mappedBy = "priceSchedules", fetch = FetchType.EAGER, cascade = { CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    private Set<Campaign> campaigns = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PriceSchedule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public PriceSchedule startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public PriceSchedule endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Set<PriceScheduleHour> getPriceScheduleHours() {
        return priceScheduleHours;
    }

    public PriceSchedule priceScheduleHours(Set<PriceScheduleHour> priceScheduleHours) {
        this.priceScheduleHours = priceScheduleHours;
        return this;
    }

    public PriceSchedule addPriceScheduleHour(PriceScheduleHour priceScheduleHour) {
        priceScheduleHours.add(priceScheduleHour);
        priceScheduleHour.setPriceSchedule(this);
        return this;
    }

    public PriceSchedule removePriceScheduleHour(PriceScheduleHour priceScheduleHour) {
        priceScheduleHours.remove(priceScheduleHour);
        priceScheduleHour.setPriceSchedule(null);
        return this;
    }

    public void setPriceScheduleHours(Set<PriceScheduleHour> priceScheduleHours) {
        this.priceScheduleHours = priceScheduleHours;
    }

    public Set<Campaign> getCampaigns() {
        return campaigns;
    }

    public PriceSchedule campaigns(Set<Campaign> campaigns) {
        this.campaigns = campaigns;
        return this;
    }

    public PriceSchedule addCampaign(Campaign campaign) {
        campaigns.add(campaign);
        campaign.getPriceSchedules().add(this);
        return this;
    }

    public PriceSchedule removeCampaign(Campaign campaign) {
        campaigns.remove(campaign);
        campaign.getPriceSchedules().remove(this);
        return this;
    }

    public void setCampaigns(Set<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceSchedule priceSchedule = (PriceSchedule) o;
        if(priceSchedule.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, priceSchedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceSchedule{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}

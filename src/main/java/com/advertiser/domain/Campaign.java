package com.advertiser.domain;

import com.advertiser.domain.enumeration.CampaignBusiness;
import com.advertiser.domain.enumeration.CampaignState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Campaign.
 */
@Entity
@Table(name = "campaign")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_short")
    private String nameShort;

    @Column(name = "product")
    private String product;

    @Column(name = "spot_amount")
    private Integer spotAmount;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "color")
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_state")
    private CampaignState campaignState;

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_business")
    private CampaignBusiness campaignBusiness;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Spot> spots = new HashSet<>();

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<SpotInfo> spotInfos = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "campaign_price_schedule",
               joinColumns = @JoinColumn(name="campaigns_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="price_schedules_id", referencedColumnName="ID"))
    private Set<PriceSchedule> priceSchedules = new HashSet<>();

    @ManyToMany(mappedBy = "campaigns", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Report> reports = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Campaign name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public Campaign nameShort(String nameShort) {
        this.nameShort = nameShort;
        return this;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getProduct() {
        return product;
    }

    public Campaign product(String product) {
        this.product = product;
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getSpotAmount() {
        return spotAmount;
    }

    public Campaign spotAmount(Integer spotAmount) {
        this.spotAmount = spotAmount;
        return this;
    }

    public void setSpotAmount(Integer spotAmount) {
        this.spotAmount = spotAmount;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public Campaign startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Campaign endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CampaignState getCampaignState() {
        return campaignState;
    }

    public void setCampaignState(CampaignState campaignState) {
        this.campaignState = campaignState;
    }

    /*public Business getBusiness() {
        return business;
    }

    public Campaign business(Business business) {
        this.business = business;
        return this;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }*/

    public CampaignBusiness getCampaignBusiness() {
        return campaignBusiness;
    }

    public void setCampaignBusiness(CampaignBusiness campaignBusiness) {
        this.campaignBusiness = campaignBusiness;
    }

    public Set<Spot> getSpots() {
        return spots;
    }

    public Campaign spots(Set<Spot> spots) {
        this.spots = spots;
        return this;
    }

    public Campaign addSpot(Spot spot) {
        spots.add(spot);
        spot.setCampaign(this);
        return this;
    }

    public Campaign removeSpot(Spot spot) {
        spots.remove(spot);
        spot.setCampaign(null);
        return this;
    }

    public void setSpots(Set<Spot> spots) {
        this.spots = spots;
    }

    public Set<SpotInfo> getSpotInfos() {
        return spotInfos;
    }

    public Campaign spotInfos(Set<SpotInfo> spotInfos) {
        this.spotInfos = spotInfos;
        return this;
    }

    public Campaign addSpotInfo(SpotInfo spotInfo) {
        spotInfos.add(spotInfo);
        spotInfo.setCampaign(this);
        return this;
    }

    public Campaign removeSpotInfo(SpotInfo spotInfo) {
        spotInfos.remove(spotInfo);
        spotInfo.setCampaign(null);
        return this;
    }

    public void setSpotInfos(Set<SpotInfo> spotInfos) {
        this.spotInfos = spotInfos;
    }

    public Set<PriceSchedule> getPriceSchedules() {
        return priceSchedules;
    }

    public Campaign priceSchedules(Set<PriceSchedule> priceSchedules) {
        this.priceSchedules = priceSchedules;
        return this;
    }

    public Campaign addPriceSchedule(PriceSchedule priceSchedule) {
        priceSchedules.add(priceSchedule);
        priceSchedule.getCampaigns().add(this);
        return this;
    }

    public Campaign removePriceSchedule(PriceSchedule priceSchedule) {
        priceSchedules.remove(priceSchedule);
        priceSchedule.getCampaigns().remove(this);
        return this;
    }

    public void setPriceSchedules(Set<PriceSchedule> priceSchedules) {
        this.priceSchedules = priceSchedules;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public Campaign reports(Set<Report> reports) {
        this.reports = reports;
        return this;
    }

    public Campaign addReport(Report report) {
        reports.add(report);
        report.getCampaigns().add(this);
        return this;
    }

    public Campaign removeReport(Report report) {
        reports.remove(report);
        report.getCampaigns().remove(this);
        return this;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Campaign campaign = (Campaign) o;
        if(campaign.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, campaign.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", nameShort='" + nameShort + "'" +
            ", product='" + product + "'" +
            ", spotAmount='" + spotAmount + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}

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
 * A SpotInfo.
 */
@Entity
@Table(name = "spot_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SpotInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "length")
    private Double length;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "producer")
    private String producer;

    @Column(name = "scenario_author")
    private String scenarioAuthor;

    @Column(name = "sound_author")
    private String soundAuthor;

    @Column(name = "performer")
    private String performer;

    @Column(name = "music")
    private String music;

    @OneToMany(mappedBy = "spotInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Spot> spots = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    private Campaign campaign;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLength() {
        return length;
    }

    public SpotInfo length(Double length) {
        this.length = length;
        return this;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getFilePath() {
        return filePath;
    }

    public SpotInfo filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getProducer() {
        return producer;
    }

    public SpotInfo producer(String producer) {
        this.producer = producer;
        return this;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getScenarioAuthor() {
        return scenarioAuthor;
    }

    public SpotInfo scenarioAuthor(String scenarioAuthor) {
        this.scenarioAuthor = scenarioAuthor;
        return this;
    }

    public void setScenarioAuthor(String scenarioAuthor) {
        this.scenarioAuthor = scenarioAuthor;
    }

    public String getSoundAuthor() {
        return soundAuthor;
    }

    public SpotInfo soundAuthor(String soundAuthor) {
        this.soundAuthor = soundAuthor;
        return this;
    }

    public void setSoundAuthor(String soundAuthor) {
        this.soundAuthor = soundAuthor;
    }

    public String getPerformer() {
        return performer;
    }

    public SpotInfo performer(String performer) {
        this.performer = performer;
        return this;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getMusic() {
        return music;
    }

    public SpotInfo music(String music) {
        this.music = music;
        return this;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public Set<Spot> getSpots() {
        return spots;
    }

    public SpotInfo spots(Set<Spot> spots) {
        this.spots = spots;
        return this;
    }

    public SpotInfo addSpot(Spot spot) {
        spots.add(spot);
        spot.setSpotInfo(this);
        return this;
    }

    public SpotInfo removeSpot(Spot spot) {
        spots.remove(spot);
        spot.setSpotInfo(null);
        return this;
    }

    public void setSpots(Set<Spot> spots) {
        this.spots = spots;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public SpotInfo campaign(Campaign campaign) {
        this.campaign = campaign;
        return this;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpotInfo spotInfo = (SpotInfo) o;
        if(spotInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, spotInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpotInfo{" +
            "id=" + id +
            ", length='" + length + "'" +
            ", filePath='" + filePath + "'" +
            ", producer='" + producer + "'" +
            ", scenarioAuthor='" + scenarioAuthor + "'" +
            ", soundAuthor='" + soundAuthor + "'" +
            ", performer='" + performer + "'" +
            ", music='" + music + "'" +
            '}';
    }
}

package com.advertiser.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the SpotInfo entity.
 */
public class SpotInfoDTO implements Serializable {

    private Long id;

    private Double length;

    private String filePath;

    private String producer;

    private String scenarioAuthor;

    private String soundAuthor;

    private String performer;

    private String music;


    private Long campaignId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }
    public String getScenarioAuthor() {
        return scenarioAuthor;
    }

    public void setScenarioAuthor(String scenarioAuthor) {
        this.scenarioAuthor = scenarioAuthor;
    }
    public String getSoundAuthor() {
        return soundAuthor;
    }

    public void setSoundAuthor(String soundAuthor) {
        this.soundAuthor = soundAuthor;
    }
    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }
    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpotInfoDTO spotInfoDTO = (SpotInfoDTO) o;

        if ( ! Objects.equals(id, spotInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpotInfoDTO{" +
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

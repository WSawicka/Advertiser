package com.advertiser.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the PriceScheduleHour entity.
 */
public class PriceScheduleHourDTO implements Serializable {

    private Long id;

    private Integer hour;

    private BigDecimal price;


    private Long priceScheduleId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getPriceScheduleId() {
        return priceScheduleId;
    }

    public void setPriceScheduleId(Long priceScheduleId) {
        this.priceScheduleId = priceScheduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PriceScheduleHourDTO priceScheduleHourDTO = (PriceScheduleHourDTO) o;

        if ( ! Objects.equals(id, priceScheduleHourDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceScheduleHourDTO{" +
            "id=" + id +
            ", hour='" + hour + "'" +
            ", price='" + price + "'" +
            '}';
    }
}

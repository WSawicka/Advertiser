package com.advertiser.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A PriceScheduleHour.
 */
@Entity
@Table(name = "price_schedule_hour")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PriceScheduleHour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "hour")
    private Integer hour;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @ManyToOne
    private PriceSchedule priceSchedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHour() {
        return hour;
    }

    public PriceScheduleHour hour(Integer hour) {
        this.hour = hour;
        return this;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PriceScheduleHour price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PriceSchedule getPriceSchedule() {
        return priceSchedule;
    }

    public PriceScheduleHour priceSchedule(PriceSchedule priceSchedule) {
        this.priceSchedule = priceSchedule;
        return this;
    }

    public void setPriceSchedule(PriceSchedule priceSchedule) {
        this.priceSchedule = priceSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceScheduleHour priceScheduleHour = (PriceScheduleHour) o;
        if(priceScheduleHour.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, priceScheduleHour.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceScheduleHour{" +
            "id=" + id +
            ", hour='" + hour + "'" +
            ", price='" + price + "'" +
            '}';
    }
}

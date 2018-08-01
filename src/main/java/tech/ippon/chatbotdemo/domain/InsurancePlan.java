package tech.ippon.chatbotdemo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InsurancePlan.
 */
@Entity
@Table(name = "insurance_plan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InsurancePlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "trip_cancellation")
    private Integer tripCancellation;

    @Column(name = "emergency_transportation")
    private Integer emergencyTransportation;

    @Column(name = "bagage_lost")
    private Integer bagageLost;

    @Column(name = "concierge_service")
    private Boolean conciergeService;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public InsurancePlan name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public InsurancePlan price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTripCancellation() {
        return tripCancellation;
    }

    public InsurancePlan tripCancellation(Integer tripCancellation) {
        this.tripCancellation = tripCancellation;
        return this;
    }

    public void setTripCancellation(Integer tripCancellation) {
        this.tripCancellation = tripCancellation;
    }

    public Integer getEmergencyTransportation() {
        return emergencyTransportation;
    }

    public InsurancePlan emergencyTransportation(Integer emergencyTransportation) {
        this.emergencyTransportation = emergencyTransportation;
        return this;
    }

    public void setEmergencyTransportation(Integer emergencyTransportation) {
        this.emergencyTransportation = emergencyTransportation;
    }

    public Integer getBagageLost() {
        return bagageLost;
    }

    public InsurancePlan bagageLost(Integer bagageLost) {
        this.bagageLost = bagageLost;
        return this;
    }

    public void setBagageLost(Integer bagageLost) {
        this.bagageLost = bagageLost;
    }

    public Boolean isConciergeService() {
        return conciergeService;
    }

    public InsurancePlan conciergeService(Boolean conciergeService) {
        this.conciergeService = conciergeService;
        return this;
    }

    public void setConciergeService(Boolean conciergeService) {
        this.conciergeService = conciergeService;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InsurancePlan insurancePlan = (InsurancePlan) o;
        if (insurancePlan.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insurancePlan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsurancePlan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", tripCancellation=" + getTripCancellation() +
            ", emergencyTransportation=" + getEmergencyTransportation() +
            ", bagageLost=" + getBagageLost() +
            ", conciergeService='" + isConciergeService() + "'" +
            "}";
    }
}

package tech.ippon.chatbotdemo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Travel.
 */
@Entity
@Table(name = "travel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Travel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_arrival")
    private String countryArrival;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "traveler_age")
    private Integer travelerAge;

    @Column(name = "state_departure")
    private String stateDeparture;

    @Column(name = "insurance_id")
    private Integer insuranceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryArrival() {
        return countryArrival;
    }

    public Travel countryArrival(String countryArrival) {
        this.countryArrival = countryArrival;
        return this;
    }

    public void setCountryArrival(String countryArrival) {
        this.countryArrival = countryArrival;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public Travel departureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Travel returnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getTravelerAge() {
        return travelerAge;
    }

    public Travel travelerAge(Integer travelerAge) {
        this.travelerAge = travelerAge;
        return this;
    }

    public void setTravelerAge(Integer travelerAge) {
        this.travelerAge = travelerAge;
    }

    public String getStateDeparture() {
        return stateDeparture;
    }

    public Travel stateDeparture(String stateDeparture) {
        this.stateDeparture = stateDeparture;
        return this;
    }

    public void setStateDeparture(String stateDeparture) {
        this.stateDeparture = stateDeparture;
    }

    public Integer getInsuranceId() {
        return insuranceId;
    }

    public Travel insuranceId(Integer insuranceId) {
        this.insuranceId = insuranceId;
        return this;
    }

    public void setInsuranceId(Integer insuranceId) {
        this.insuranceId = insuranceId;
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
        Travel travel = (Travel) o;
        if (travel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), travel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Travel{" +
            "id=" + getId() +
            ", countryArrival='" + getCountryArrival() + "'" +
            ", departureDate='" + getDepartureDate() + "'" +
            ", returnDate='" + getReturnDate() + "'" +
            ", travelerAge=" + getTravelerAge() +
            ", stateDeparture='" + getStateDeparture() + "'" +
            ", insuranceId=" + getInsuranceId() +
            "}";
    }
}

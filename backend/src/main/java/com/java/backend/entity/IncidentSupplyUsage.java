package com.java.backend.entity;

import java.io.Serializable;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "incident_supply_usage")
public class IncidentSupplyUsage {

    @EmbeddedId
    private IncidentSupplyUsageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("incidentId")
    @JoinColumn(name = "incident_id")
    private HealthIncident incident;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("supplyId")
    @JoinColumn(name = "supply_id")
    private MedicalSupply supply;

    @Column(nullable = false)
    private Integer quantityUsed;

    // Khóa chính phức hợp
    @Embeddable
    public static class IncidentSupplyUsageId implements Serializable {
        private Long incidentId;
        private Long supplyId;

        // Constructors (optional but often useful)
        public IncidentSupplyUsageId() {
        }

        public IncidentSupplyUsageId(Long incidentId, Long supplyId) {
            this.incidentId = incidentId;
            this.supplyId = supplyId;
        }

        // Getters and Setters for IncidentSupplyUsageId
        public Long getIncidentId() {
            return incidentId;
        }

        public void setIncidentId(Long incidentId) {
            this.incidentId = incidentId;
        }

        public Long getSupplyId() {
            return supplyId;
        }

        public void setSupplyId(Long supplyId) {
            this.supplyId = supplyId;
        }

        // hashCode and equals methods
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            IncidentSupplyUsageId that = (IncidentSupplyUsageId) o;
            return Objects.equals(incidentId, that.incidentId) &&
                    Objects.equals(supplyId, that.supplyId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(incidentId, supplyId);
        }
    }

    // Getters and Setters for IncidentSupplyUsage
    public IncidentSupplyUsageId getId() {
        return id;
    }

    public void setId(IncidentSupplyUsageId id) {
        this.id = id;
    }

    public HealthIncident getIncident() {
        return incident;
    }

    public void setIncident(HealthIncident incident) {
        this.incident = incident;
    }

    public MedicalSupply getSupply() {
        return supply;
    }

    public void setSupply(MedicalSupply supply) {
        this.supply = supply;
    }

    public Integer getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(Integer quantityUsed) {
        this.quantityUsed = quantityUsed;
    }
}
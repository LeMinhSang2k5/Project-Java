package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.backend.entity.IncidentSupplyUsage;
import com.java.backend.entity.IncidentSupplyUsage.IncidentSupplyUsageId;

public interface IncidentSupplyUsageRepository extends JpaRepository<IncidentSupplyUsage, IncidentSupplyUsageId> {

    @Modifying
    @Query("DELETE FROM IncidentSupplyUsage u WHERE u.incident.id = :incidentId")
    void deleteByIncidentId(@Param("incidentId") Long incidentId);
}

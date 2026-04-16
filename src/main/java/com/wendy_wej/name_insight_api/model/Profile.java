package com.wendy_wej.name_insight_api.model;

import jakarta.persistence.*;

import java.time.Instant;
import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Profile {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable=false)
    private String gender;

    @Column(nullable=false)
    private Integer age;

    @Column(name = "age_group", nullable=false)
    private String ageGroup;

    @Column(name = "country_id", nullable=false)
    private String countryId;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.createdAt = Instant.now();
    }
}

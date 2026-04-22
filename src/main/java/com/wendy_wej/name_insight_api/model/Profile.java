package com.wendy_wej.name_insight_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @Column(name = "gender_probability", nullable=false)
    private Double genderProbability;

    @Column(name = "sample_size", nullable=false)
    private Integer sampleSize;

    @Column(nullable=false)
    private Integer age;

    @Column(name = "age_group", nullable=false)
    private String ageGroup;

    @Column(name = "country_id", nullable=false)
    private String countryId;

    @Column(name = "country_probability", nullable=false)
    private Double countryProbability;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.createdAt = Instant.now();
    }
}

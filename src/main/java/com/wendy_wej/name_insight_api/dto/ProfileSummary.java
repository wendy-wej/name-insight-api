package com.wendy_wej.name_insight_api.dto;

import com.wendy_wej.name_insight_api.model.Profile;
import lombok.Data;

import java.util.UUID;

@Data
public class ProfileSummary {
    private UUID id;
    private String name;
    private String gender;
    private Integer age;
    private String ageGroup;
    private String countryId;

    public static ProfileSummary from(Profile profile) {
        ProfileSummary summary = new ProfileSummary();
        summary.setId(profile.getId());
        summary.setName(profile.getName());
        summary.setGender(profile.getGender());
        summary.setAge(profile.getAge());
        summary.setAgeGroup(profile.getAgeGroup());
        summary.setCountryId(profile.getCountryId());
        return summary;
    }
}
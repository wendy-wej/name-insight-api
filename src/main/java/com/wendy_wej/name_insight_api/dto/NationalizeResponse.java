package com.wendy_wej.name_insight_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NationalizeResponse {
    private Integer count;
    private Country[] country;

    @Data
    public static class Country {
        @JsonProperty("country_id")
        private String countryId;
        private Double probability;
    }
}
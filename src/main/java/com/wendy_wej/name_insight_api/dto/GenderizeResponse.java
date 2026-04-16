package com.wendy_wej.name_insight_api.dto;

import lombok.Data;

@Data
public class GenderizeResponse {
    private Integer count;
    private String gender;
    private Double probability;
}

package com.wendy_wej.name_insight_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"status", "message"})
public class ErrorResponse {
    private String status;
    private String message;
}

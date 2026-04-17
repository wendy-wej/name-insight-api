package com.wendy_wej.name_insight_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Integer count;
}

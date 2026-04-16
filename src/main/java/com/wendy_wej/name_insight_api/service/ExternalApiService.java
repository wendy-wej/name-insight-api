package com.wendy_wej.name_insight_api.service;

import com.wendy_wej.name_insight_api.dto.AgifyResponse;
import com.wendy_wej.name_insight_api.dto.GenderizeResponse;
import com.wendy_wej.name_insight_api.dto.NationalizeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalApiService {
    private final RestClient restClient = RestClient.create();


    public GenderizeResponse getGender(String name) {
        return restClient.get()
                .uri("https://api.genderize.io?name={name}", name)
                .retrieve()
                .body(GenderizeResponse.class);
    }

    public AgifyResponse getAge(String name) {
        return restClient.get()
                .uri("https://api.agify.io?name={name}", name)
                .retrieve()
                .body(AgifyResponse.class);
    }

    public NationalizeResponse getNationality(String name) {
        return restClient.get()
                .uri("https://api.nationalize.io?name={name}", name)
                .retrieve()
                .body(NationalizeResponse.class);
    }
}

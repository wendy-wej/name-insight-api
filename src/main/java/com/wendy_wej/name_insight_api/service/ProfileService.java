package com.wendy_wej.name_insight_api.service;

import com.wendy_wej.name_insight_api.dto.AgifyResponse;
import com.wendy_wej.name_insight_api.dto.GenderizeResponse;
import com.wendy_wej.name_insight_api.dto.NationalizeResponse;
import com.wendy_wej.name_insight_api.exception.ExternalApiException;
import com.wendy_wej.name_insight_api.exception.ProfileNotFoundException;
import com.wendy_wej.name_insight_api.model.Profile;
import com.wendy_wej.name_insight_api.repository.ProfileRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ExternalApiService externalApiService;

    public ProfileService(ProfileRepository profileRepository,
                          ExternalApiService externalApiService) {
        this.profileRepository = profileRepository;
        this.externalApiService = externalApiService;
    }

    public Profile createProfile(String name) {

//        1. Check if a profile with that name already exists (use ProfileRepository)
//        2. If yes → return it as-is
//        3. If no → call all three external APIs
//        4. Validate the responses (null gender, null age, empty countries = 502)
//        5. Classify age into age group
//        6. Pick the top country by probability
//        7. Build and save the Profile

        Optional<Profile> existingProfile = profileRepository.findByNameIgnoreCase(name);
        if (existingProfile.isPresent()) {
            return existingProfile.get();
        }

            GenderizeResponse genderizeResponse = externalApiService.getGender(name);
            AgifyResponse agifyResponse = externalApiService.getAge(name);
            NationalizeResponse nationalizeResponse = externalApiService.getNationality(name);

            if (genderizeResponse == null || genderizeResponse.getGender() == null || genderizeResponse.getCount() == null || genderizeResponse.getCount() == 0) {
                throw new ExternalApiException("Genderize returned an invalid response");

            }
            if (agifyResponse == null || agifyResponse.getAge() == null) {
                throw new ExternalApiException("Agify returned an invalid response");
            }

            if (nationalizeResponse == null || nationalizeResponse.getCountry() == null || nationalizeResponse.getCountry().length == 0) {
                throw new ExternalApiException("Nationalize returned an invalid response");
            }

            int age = agifyResponse.getAge();
            String ageGroup;
            if (age <= 12) {
                ageGroup = "child";
            } else if (age <= 19) {
                ageGroup = "teenager";
            } else if (age <= 59) {
                ageGroup = "adult";
            } else {
                ageGroup = "senior";
            }

            // pick top country by probability
            NationalizeResponse.Country topCountry = null;
            for (NationalizeResponse.Country c : nationalizeResponse.getCountry()) {
                if (topCountry == null || c.getProbability() > topCountry.getProbability()) {
                    topCountry = c;
                }
            }

            Profile profile = new Profile();
            profile.setName(name);
            profile.setGender(genderizeResponse.getGender());
            profile.setGenderProbability(genderizeResponse.getProbability());
            profile.setSampleSize(genderizeResponse.getCount());
            profile.setAge(age);
            profile.setAgeGroup(ageGroup);
            if (topCountry != null) {
                profile.setCountryId(topCountry.getCountryId());
                profile.setCountryProbability(topCountry.getProbability());
            }

            return profileRepository.save(profile);
    }

    public Profile getProfileById(UUID id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));
    }

    public List<Profile> getAllProfiles(String gender, String countryId, String ageGroup){
        Specification<Profile> spec = Specification.where((Specification<Profile>) null);

        if (gender != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("gender")), gender.toLowerCase()));
        }

        if (countryId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("countryId")), countryId.toLowerCase()));
        }

        if (ageGroup != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("ageGroup")), ageGroup.toLowerCase()));
        }

        return profileRepository.findAll(spec);
    }

    public void deleteProfile(UUID id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));
        profileRepository.delete(profile);
    }
}

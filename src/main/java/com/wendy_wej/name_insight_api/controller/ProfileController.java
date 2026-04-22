package com.wendy_wej.name_insight_api.controller;

import com.wendy_wej.name_insight_api.dto.ApiResponse;
import com.wendy_wej.name_insight_api.dto.ProfileSummary;
import com.wendy_wej.name_insight_api.model.Profile;
import com.wendy_wej.name_insight_api.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody Map<String, Object> body) {
// 422 — name exists but is not a String
        Object nameValue = body.get("name");
        if (nameValue != null && !(nameValue instanceof String)) {
            return ResponseEntity.status(422)
                    .body(new ApiResponse<>("error", "name must be a string", null, null));
        }

        // 400 — missing, null, or blank name
        String name = (String) nameValue;
        if (name == null || name.isBlank()) {
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>("error", "name is required", null, null));
        }

        ProfileService.ProfileResult result = profileService.createProfile(name);
        if (result.isNew()) {
            return ResponseEntity
                    .status(201)
                    .body(new ApiResponse<>(
                            "success",
                            null,
                            result.getProfile(),
                            null
                    ));
        } else {
            return ResponseEntity
                    .status(200)
                    .body(new ApiResponse<>(
                            "success",
                            "Profile already exists",
                            result.getProfile(),
                            null
                    ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable UUID id) {
        Profile profile = profileService.getProfileById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", null, profile, null));
    }

    @GetMapping
    public ResponseEntity<?> getAllProfiles(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false, name = "country_id") String countryId,
            @RequestParam(required = false, name = "age_group") String ageGroup){
        List<ProfileSummary> profiles = profileService.getAllProfiles(gender, countryId, ageGroup)
                .stream().map(ProfileSummary::from).toList();
        return ResponseEntity.ok(new ApiResponse<>("success", null, profiles, profiles.size()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable UUID id){
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}

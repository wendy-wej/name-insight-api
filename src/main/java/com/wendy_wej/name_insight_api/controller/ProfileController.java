package com.wendy_wej.name_insight_api.controller;

import com.wendy_wej.name_insight_api.model.Profile;
import com.wendy_wej.name_insight_api.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        String name = (String) body.get("name");
        Profile createdProfile = profileService.createProfile(name);
        return ResponseEntity.ok(createdProfile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable UUID id) {
        Profile profile = profileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping
    public ResponseEntity<?> getAllProfiles(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String countryId,
            @RequestParam(required = false) String ageGroup){
        return ResponseEntity.ok(profileService.getAllProfiles(gender, countryId, ageGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable UUID id){
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}

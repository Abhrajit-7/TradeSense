package com.funWithStocks.FunWithStocks.controller;

import com.funWithStocks.FunWithStocks.dto.ProfileDTO;
import com.funWithStocks.FunWithStocks.entity.UserProfile;
import com.funWithStocks.FunWithStocks.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{username}")
    public ProfileDTO getUserProfileByUsername(@PathVariable String username) {
        return userProfileService.getUserProfileByUsername(username);
    }

    @PostMapping("/{username}")
    public UserProfile createUserProfile(@RequestBody ProfileDTO userProfile, @PathVariable String username) {
        return userProfileService.createUserProfile(userProfile, username);
    }

    @PutMapping("/{username}")
    public ProfileDTO updateUserProfileByUsername(@PathVariable String username, @RequestBody ProfileDTO userProfile) {
        return userProfileService.updateUserProfileByUsername(username, userProfile);
    }
}


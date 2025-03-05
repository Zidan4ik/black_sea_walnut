package org.example.black_sea_walnut.controller.user;

import jakarta.persistence.EntityNotFoundException;
import org.example.black_sea_walnut.controller.AuthorizationController;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "org.example.black_sea_walnut.controller.user",basePackageClasses = AuthorizationController.class)
public class WebGlobalUserAdvice {
    private final UserService userService;
    public WebGlobalUserAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return userService.getByEmail(userDetails.getUsername()).orElseThrow(
                    () -> new EntityNotFoundException("User with email: " + userDetails.getUsername() + " was not found!")
            );
        }
        return null;
    }
}
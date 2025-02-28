package org.example.black_sea_walnut.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class AuthorizationController {

    @GetMapping("/login")
    public ModelAndView viewRegistration() {
        return new ModelAndView("web/authorize/login");
    }

    @GetMapping("/registration")
    public ModelAndView viewRegistration2() {
        return new ModelAndView("web/authorize/registration");
    }

    @GetMapping("/terms-of-use")
    public ModelAndView viewTermsOfUse() {
        return new ModelAndView("web/terms-of-use");
    }

    @GetMapping("/thanks")
    public ModelAndView viewThanks() {
        return new ModelAndView("web/thanks");
    }

    @GetMapping("/password-recovery")
    public ModelAndView viewPasswordRecovery() {
        return new ModelAndView("web/password-recovery");
    }
}

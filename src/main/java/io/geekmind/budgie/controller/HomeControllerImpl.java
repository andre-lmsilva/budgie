package io.geekmind.budgie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeControllerImpl {

    @GetMapping("/")
    public ModelAndView showHome(ModelAndView requestContext) {
        return this.showLoginForm(requestContext);
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(ModelAndView requestContext) {
        requestContext.setViewName("home/login");
        return requestContext;
    }

}

package projekti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    // default --> profile
    @GetMapping("*")
    public String toAccount() {
        return "register";
    }
/*
    @GetMapping("*")
    public String helloWorld(Model model) {
        model.addAttribute("message", "World!");
        return "index";
    }*/
}

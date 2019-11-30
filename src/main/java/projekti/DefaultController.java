package projekti;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    // default --> profile
    @GetMapping("*")
    public String toProfile() {
        return "profile";
    }
/*
    @GetMapping("*")
    public String helloWorld(Model model) {
        model.addAttribute("message", "World!");
        return "index";
    }*/
}

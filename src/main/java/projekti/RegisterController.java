package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String addAccount(@RequestParam String username, @RequestParam String password) {

        // error parameters here, if username already in use etc
        if (accountRepository.findByUsername(username) != null) {
            return "redirect:/register";
        }

        // login succesful
        Account a = new Account(username, passwordEncoder.encode(password));
        accountRepository.save(a);
        return "redirect:/login";
    }





}

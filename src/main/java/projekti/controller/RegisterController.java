package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.model.Account;
import projekti.repository.AccountRepository;

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
    public String addAccount(@RequestParam String username, @RequestParam String password, @RequestParam String nickname) {

        // error parameters here, if username already in use etc
        if (accountRepository.findByUsername(username) != null) {
            return "redirect:/register";
        }

        // create a new account
        Account a = new Account(username, passwordEncoder.encode(password), nickname);
        accountRepository.save(a);
        return "redirect:/login";
    }





}

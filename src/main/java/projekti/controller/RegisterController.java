package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String addAccount(@RequestParam String name, @RequestParam String username, @RequestParam String password, @RequestParam String nickname) {

        // check for too short credentials
        if (name.length() < 4 || username.length() < 4 || password.length() < 4 || nickname.length() < 4) {
            return "redirect:/register";
        }

        // check if username or nickname is already in use
        if (accountRepository.findByUsername(username) != null || accountRepository.findByNickname(nickname) != null) {
            return "redirect:/register";
        }

        // credentials OK --> create a new account!
        Account a = new Account(name, username, passwordEncoder.encode(password), nickname);
        accountRepository.save(a);
        return "redirect:/login";

    }





}

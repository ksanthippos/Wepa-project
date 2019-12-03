package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;



    @GetMapping("/account")
    public String listAll(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "account";
    }

    @GetMapping("/account/{id}")
    public String getOne(Model model, @PathVariable Long id) {

        model.addAttribute("account", accountRepository.getOne(id));
        return "account";

    }










}

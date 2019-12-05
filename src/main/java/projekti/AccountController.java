package projekti;

import org.apache.xpath.operations.Mod;
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

    @GetMapping("/users")
    public String getAll(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "users";
    }

    @GetMapping("/{nickname}")
    public String getFriend(Model model, @PathVariable String nickname) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        if (me.getNickname().equals(nickname)) {
            return "/mypage";
        }

        return "/friendspage";

    }

    @GetMapping("/mypage")
    public String getMyPage() {
        return "/mypage";
    }




















}

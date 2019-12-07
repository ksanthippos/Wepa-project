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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;


    // list of all users
    @GetMapping("/users")
    public String getAll(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "users";
    }

    // mapping for URL expression .../nickname
    @GetMapping("/account/{nickname}")
    public String getFriend(Model model, @PathVariable String nickname) {

        // user authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        // if user clicks his own profile from the list --> own profile is shown
        if (me.getNickname().equals(nickname)) {
            model.addAttribute("account", me);
            return "mypage";
        }

        // user sees only friend view from other users
        Account friend = accountRepository.findByNickname(nickname);
        model.addAttribute("account", friend);
        return "friendspage";

    }

    // separate page for profile (if not selected on the users list), gets redirected via previous GET-request
    @GetMapping("/mypage")
    public String getMyPage(Model model) {

        // user authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        model.addAttribute("account", me);
        return "redirect:/account/" + me.getNickname();
    }



}

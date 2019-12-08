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

        // user authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("user", me);

        return "users";
    }

    // mapping for URL expression .../nickname
    @GetMapping("/account/{nickname}")
    public String getFriend(Model model, @PathVariable String nickname) {

        // user authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);
        Account other = accountRepository.findByNickname(nickname);

        // if user clicks his own profile from the list --> own profile is shown
        if (me.getNickname().equals(nickname)) {
            model.addAttribute("user", me);
            return "mypage";
        }

        // profile is followed --> view profile
        for (Account a: me.getFollowingAt()) {
            if (a.getId() == other.getId()) {   // account ids are unique

                model.addAttribute("account", me);  // THIS EVEN NEEDED?
                model.addAttribute("friend", other);    // target

                return "friendspage";
            }
        }

        // not following --> redirect back
        return "redirect:/users";

    }

    // separate page for profile (if not selected on the users list), gets redirected via previous GET-request
    @GetMapping("/mypage")
    public String getMyPage(Model model) {

        // user authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        model.addAttribute("user", me);
        return "redirect:/account/" + me.getNickname();
    }


    // add follower from the list
    @PostMapping("/users/{id}")
    public String addFriend(@PathVariable Long id) {

        // user authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);
        Account other = accountRepository.getOne(id);

        // is hidden in thymeleaf but just a backup: an account should'nt follow itself
        if (me.getId() == other.getId()) {
            return "redirect:/mypage";
        }

        // user is already following --> redirect
        for (Account a: me.getFollowingAt()) {
            if (a.getId() == other.getId()) {
                return "redirect:/users";   // if user not following --> redirect
            }
        }

        me.getFollowingAt().add(other);
        other.getFollowingMe().add(me);

        accountRepository.save(me);
        accountRepository.save(other);

        return "redirect:/users";

    }



}

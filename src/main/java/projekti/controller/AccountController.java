package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import projekti.model.Message;
import projekti.repository.AccountRepository;
import projekti.model.Account;
import projekti.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;


    // list of all users
    @GetMapping("/users")
    public String getAll(Model model) {

        Account me = accountRepository.findByUsername(authenticateUser());
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("user", me);

        return "users";
    }

    // list of friends (= followers & following profiles)
    @GetMapping("/myfriends")
    public String getAllFriends(Model model) {

        Account me = accountRepository.findByUsername(authenticateUser());
        model.addAttribute("user", me);

        return "myfriends";
    }

    // every user has own personal URL
    @GetMapping("/account/{nickname}")
    public String getFriend(Model model, @PathVariable String nickname) {

        Account me = accountRepository.findByUsername(authenticateUser());
        Account other = accountRepository.findByNickname(nickname);

        // if user clicks his own profile from the list --> own profile is shown
        if (me.getNickname().equals(nickname)) {

            getNewsFeed(model, me);
            return "mypage";
        }

        // check if account is following user
        for (Account a: other.getFollowingAt()) {
            if (me.getId() == a.getId()) {
                model.addAttribute("isFollowing", true);
            }
        }

        model.addAttribute("friend", other);
        return "friendspage";

    }

    // separate page for profile (if not selected on the users list), gets redirected via previous GET-request
    @GetMapping("/mypage")
    public String getMyPage(Model model) {

        Account me = accountRepository.findByUsername(authenticateUser());
        getNewsFeed(model, me);

        return "redirect:/account/" + me.getNickname();
    }


    // add follower from the list
    @PostMapping("/users/{id}")
    public String addFriend(@PathVariable Long id) {

        Account me = accountRepository.findByUsername(authenticateUser());
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

    // PRIVATE METHODS
    // ********************************

    // user authentication
    private String authenticateUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return username;
    }

    // all messages from followed profiles to list and to model
    private void getNewsFeed(Model model, Account me) {

        // get all messages from followed accounts
        List<Message> followedMessages = new ArrayList<>();
        for (Account a: me.getFollowingAt()) {
            for (Message m: a.getMessageList()) {
                followedMessages.add(m);
            }
        }

        List<Message> newsfeed = new ArrayList<>();
        newsfeed.addAll(me.getMessageList());
        newsfeed.addAll(followedMessages);

        model.addAttribute("user", me);
        model.addAttribute("newsfeed", newsfeed);

    }


}

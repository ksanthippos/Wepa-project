package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.extras.java8time.expression.Temporals;
import projekti.model.Comment;
import projekti.model.Message;
import projekti.repository.AccountRepository;
import projekti.model.Account;
import projekti.repository.MessageRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        getNewsFeed(model, other);
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
                return "redirect:/users";
            }
        }

        // conditions ok, set to follow
        me.getFollowingAt().add(other);
        other.getFollowingMe().add(me);

        // set following time
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String timeAsString = time.format(formatter);

        me.getFollowingAtDate().put(timeAsString, other);
        other.getFollowingMeDate().put(timeAsString, me);

        accountRepository.save(me);
        accountRepository.save(other);

        return "redirect:/users";

    }

    // blocking user
    @PostMapping("/account/{nickname}")
    public String block(@PathVariable String nickname) {

        Account me = accountRepository.findByUsername(authenticateUser());
        Account other = accountRepository.findByNickname(nickname);

        if (me.getId() == other.getId()) {
            return "redirect:/mypage";
        }

        // user is already blocked --> redirect
        for (Account a: me.getBlockedAt()) {
            if (a.getId() == other.getId()) {
                return "redirect:/account/{nickname}";
            }
        }

        // set blocking
        me.getBlockedAt().add(other);
        other.getBlockedMe().add(me);

        accountRepository.save(me);
        accountRepository.save(other);

        return "redirect:/account/{nickname}";
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
    private void getNewsFeed(Model model, Account account) {

        Account me = accountRepository.findByUsername(authenticateUser());

        // get all messages from followed accounts
        List<Message> followedMessages = new ArrayList<>();
        for (Account a: account.getFollowingAt()) {
            if (!me.getBlockedAt().contains(a)) {   // blocked users messages not shown
                for (Message m : a.getMessageList()) {
                    followedMessages.add(m);
                }
            }
        }

        // if user is blocked by someone, remove also blockers messages
        Iterator iterator = followedMessages.iterator();
        Message iterMessage;

        while (iterator.hasNext()){
            iterMessage = (Message) iterator.next();
            if (iterMessage.getAccount().getBlockedAt().contains(me)) {
                iterator.remove();
            }
        }

        List<Message> messages = new ArrayList<>();
        messages.addAll(account.getMessageList());
        messages.addAll(followedMessages);

        // messages sorted by date (newest first), only 25 messages max on a wall
        Collections.sort(messages, Comparator.comparing(message -> message.getDateTime()));
        Collections.reverse(messages);
        if (messages.size() > 25) {
            messages = messages.subList(0, 25);
        }

        // only 10 comments max per message
        for (Message m: messages) {
            Collections.sort(m.getComments(), Comparator.comparing(comment -> comment.getDateTime()));
            Collections.reverse(m.getComments());

            if (m.getComments().size() > 10) {

                m.getComments().subList(10, m.getComments().size()).clear();
            }
        }

        if (me.getId() == account.getId()) {
            model.addAttribute("user", account);
            model.addAttribute("newsfeed", messages);
        }

        else {
            model.addAttribute("user", me);
            model.addAttribute("friend", account);
            model.addAttribute("newsfeed", messages);
        }

    }


}

package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.repository.MessageRepository;
import projekti.model.Account;
import projekti.model.Message;
import projekti.repository.AccountRepository;


import java.time.LocalDateTime;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;



    // message posting allowed only on users wall
    @PostMapping("/mypage")
    public String newMessage(@RequestParam String content) {

        Account me = accountRepository.findByUsername(authenticateUser());

        Message message = new Message();

        // set content, date and account
        message.setContent(content);
        message.setDateTime(LocalDateTime.now());
        message.setAccount(me);
        me.getMessageList().add(message);

        accountRepository.save(me);
        messageRepository.save(message);

        return "redirect:/mypage";

    }

    // adding like to a message
    @PostMapping(value = "/account/{nickname}/{id}")
    public String addLike(@PathVariable String nickname, @PathVariable Long id) {

        Account me = accountRepository.findByUsername(authenticateUser());

        Message message = messageRepository.getOne(id);

        // only one like per account
        for (Message m: me.getLikedMessages()) {
            if (m.getId() == message.getId()) {
                return "redirect:/account/{nickname}";
            }
        }

        message.getLikedAccounts().add(me);
        me.getLikedMessages().add(message);
        accountRepository.save(me);
        messageRepository.save(message);

        return "redirect:/account/{nickname}";

    }


    // PRIVATE METHODS
    // ******************

    // user authentication
    private String authenticateUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return username;
    }


}

package projekti;

import org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    // posting allowed only on mypage
    @PostMapping("/mypage")
    public String newMessage(@RequestParam String content) {

        // user auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

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





}

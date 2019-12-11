package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.model.Account;
import projekti.model.Comment;
import projekti.model.Image;
import projekti.model.Message;
import projekti.repository.AccountRepository;
import projekti.repository.CommentRepository;
import projekti.repository.ImageRepository;
import projekti.repository.MessageRepository;

import javax.annotation.processing.Messager;
import java.time.LocalDateTime;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AccountRepository accountRepository;


    @PostMapping(value = "/account/{nickname}/{id}", params = "cont")
    public String commentMsg(@PathVariable String nickname, @PathVariable Long id, @RequestParam String cont) {

        Account me = accountRepository.findByUsername(authenticateUser());
        Message message = messageRepository.getOne(id);
        Comment comment = new Comment();

        comment.setContent(cont);
        comment.setDateTime(LocalDateTime.now());
        comment.setAccount(me);
        comment.setMessage(message);

        message.getComments().add(comment);
        me.getCommentList().add(comment);

        commentRepository.save(comment);
        messageRepository.save(message);
        accountRepository.save(me);

        return "redirect:/account/{nickname}";

    }

    @PostMapping(value = "/gallery/{nickname}/{id}", params = "contImg")
    public String commentImg(@PathVariable String nickname, @PathVariable Long id, @RequestParam String contImg) {

        Account me = accountRepository.findByUsername(authenticateUser());
        Image image = imageRepository.getOne(id);
        Comment comment = new Comment();

        comment.setContent(contImg);
        comment.setDateTime(LocalDateTime.now());
        comment.setAccount(me);
        comment.setImage(image);

        image.getCommentList().add(comment);
        me.getCommentList().add(comment);

        commentRepository.save(comment);
        imageRepository.save(image);
        accountRepository.save(me);

        return "redirect:/mygallery";
        // vai return "redirect:/mygallery";    ????

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

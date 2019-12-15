package projekti.controller;

import org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projekti.model.Message;
import projekti.repository.ImageRepository;
import projekti.model.Account;
import projekti.model.Image;
import projekti.repository.AccountRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AccountRepository accountRepository;



    @GetMapping("/mygallery")
    public String getMyImages(Model model) {

        Account me = accountRepository.findByUsername(authenticateUser());
        model.addAttribute("account", me);

        return "redirect:/gallery/" + me.getNickname();

    }


    // displays URL as: gallery/nickname
    @GetMapping("/gallery/{nickname}")
    public String getGallery(Model model, @PathVariable String nickname) {

        Account me = accountRepository.findByUsername(authenticateUser());

        // if user clicks his own profile from the list --> own profile is shown
        if (me.getNickname().equals(nickname)) {

            getComments(model, me); // private method
            return "mygallery";
        }

        Account friend = accountRepository.findByNickname(nickname);
        model.addAttribute("user", me);
        getComments(model, friend);
        //model.addAttribute("name", friend.getUsername());

        return "friendsgallery";
    }


    // get single image content from repo
    @GetMapping(path = "/gallery/{nickname}/{id}/content", produces = "image/jpeg")
    @ResponseBody
    public byte[] geContent(@PathVariable String nickname, @PathVariable Long id) {

        return imageRepository.getOne(id).getContent();

    }


    // only users can add more images
    @PostMapping("/mygallery")
    public String save(@RequestParam("file") MultipartFile file,
                       @RequestParam String description) throws IOException {

        Account me = accountRepository.findByUsername(authenticateUser());

        // choose correct file format and limit amount to 10 images max per gallery
        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") ||
             file.getContentType().equals("image/gif") && me.getPicGallery().size() < 10) {
            Image i = new Image();
            i.setContent(file.getBytes());
            i.setDescription(description);
            i.setAccount(me);
            me.getPicGallery().add(i);

            imageRepository.save(i);
            accountRepository.save(me);

            return "redirect:/mygallery/";
        }

        else {
            return "redirect:/mygallery/";
        }

    }


    // setting profile picture
    @PostMapping(value = "/gallery/{nickname}/{id}", params = "setProfPic")
    public String setProfPic(@PathVariable String nickname, @PathVariable Long id) {

        Account me = accountRepository.findByUsername(authenticateUser());

        // reset first
        for (Image i: me.getPicGallery()) {
            i.setProfilePic(false);
        }

        imageRepository.getOne(id).setProfilePic(true);
        me.setProfilePicId(id);

        imageRepository.save(imageRepository.getOne(id));
        accountRepository.save(me);

        return "redirect:/mygallery";
    }

    // adding like to an image
    @PostMapping(value = "/gallery/{nickname}/{id}", params = "likeImg")
    public String addLike(@PathVariable String nickname, @PathVariable Long id) {

        Account me = accountRepository.findByUsername(authenticateUser());

        Image image = imageRepository.getOne(id);

        // only one like per account
        for (Image i: me.getLikedImages()) {
            if (i.getId() == image.getId()) {
                return "redirect:/gallery/{nickname}";
            }
        }

        image.getLikedAccounts().add(me);
        me.getLikedImages().add(image);
        accountRepository.save(me);
        imageRepository.save(image);

        return "redirect:/gallery/{nickname}";

    }


/*    // delete image (users only)
    @DeleteMapping(value = "/gallery/{nickname}/{id}")
    public String deleteImage(@PathVariable String nickname, @PathVariable Long id) {

        Account me = accountRepository.findByUsername(authenticateUser());
        me.getLikedImages().remove(imageRepository.getOne(id));
        me.getPicGallery().remove(imageRepository.getOne(id));


        imageRepository.deleteById(id);
        imageRepository.saveAll(imageRepository.findAll());
        accountRepository.save(me);

        return "redirect:/gallery/{nickname}";
    }*/



    // PRIVATE METHODS
    // ******************

    // user authentication
    private String authenticateUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return username;
    }

    // only 10 comments max per image
    private void getComments(Model model, Account account) {

        Account me = accountRepository.findByUsername(authenticateUser());

        for (Image m: account.getPicGallery()) {
            Collections.sort(m.getCommentList(), Comparator.comparing(comment -> comment.getDateTime()));
            Collections.reverse(m.getCommentList());

            if (m.getCommentList().size() > 10) {
                m.getCommentList().subList(10, m.getCommentList().size()).clear();
            }
        }

        if (me.getId() == account.getId()) {
            model.addAttribute("account", me);
        }

        else {
            model.addAttribute("account", account);
        }
    }


}

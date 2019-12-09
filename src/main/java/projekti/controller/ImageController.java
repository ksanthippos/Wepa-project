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
import projekti.repository.ImageRepository;
import projekti.model.Account;
import projekti.model.Image;
import projekti.repository.AccountRepository;

import java.io.IOException;

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

            model.addAttribute("account", me);
            return "mygallery";
        }

        Account friend = accountRepository.findByNickname(nickname);
        model.addAttribute("name", accountRepository.findByNickname(nickname).getUsername());
        model.addAttribute("account", friend);

        return "friendsgallery";
    }


    // get single image content from repo
    @GetMapping(path = "/gallery/{nickname}/{id}/content", produces = "image/jpeg")
    @ResponseBody
    public byte[] geContent(@PathVariable String nickname, @PathVariable Long id) {

        return imageRepository.getOne(id).getContent();

    }


    // user can edit only own profile gallery
    @PostMapping("/mygallery")
    public String save(@RequestParam("file") MultipartFile file,
                       @RequestParam String description) throws IOException {

        Account me = accountRepository.findByUsername(authenticateUser());

        // accept only .jpeg
        if (file.getContentType().equals("image/jpeg")) {
            Image i = new Image();
            i.setContent(file.getBytes());
            i.setDescription(description);
            i.setAccount(me);
            me.getPicGallery().add(i);

            imageRepository.save(i);
            accountRepository.save(me);

        }

        return "redirect:/mygallery/";
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

package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AccountRepository accountRepository;


    @GetMapping("/mygallery")
    public String getMyImages(Model model) {

        // user auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        model.addAttribute("account", me);
        model.addAttribute("images", me.getPicGallery());

        return "redirect:/gallery" + me.getNickname();
    }

    // displays URL as: gallery/nickname
    @GetMapping("/gallery/{nickname}")
    public String getGallery(Model model, @PathVariable String nickname) {

        // user auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        // if user clicks his own profile from the list --> own profile is shown
        if (me.getNickname().equals(nickname)) {
            model.addAttribute("account", me);
            return "mygallery";
        }

        // user sees only friend view from other users
        Account friend = accountRepository.findByNickname(nickname);
        model.addAttribute("account", friend);
        return "friendsgallery";

    }

    // user can edit only own profile gallery
    @PostMapping("/mygallery")
    public String save(@RequestParam("file") MultipartFile file,
                       @RequestParam String description) throws IOException {

        // user auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

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

        return "redirect:/gallery/" + me.getNickname();
    }

}

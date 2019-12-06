package projekti;

import org.apache.xpath.operations.Mod;
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
    public String getimages(Model model) {

        // user auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        model.addAttribute("images", me.getPicGallery());

        return "mygallery";
    }


    @PostMapping("/mygallery")
    public String save(@RequestParam("file") MultipartFile file) throws IOException {

        // user auth
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account me = accountRepository.findByUsername(username);

        // accept only .jpeg
        if (file.getContentType().equals("image/jpeg")) {
            Image i = new Image();
            i.setContent(file.getBytes());
            i.setAccount(me);
            imageRepository.save(i);
            me.getPicGallery().add(i);
            accountRepository.save(me);

        }

        return "redirect:/mygallery";
    }


}

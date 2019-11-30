package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/images")
    public String home() {
        return "redirect:/images/1";
    }

    @GetMapping("/{id}/profile")
    public String back() {
        return "redirect:/profile";
    }

    @GetMapping("images/{id}")
    public String getById(Model model, @PathVariable Long id) {

        if (imageRepository.findAll().size() > 0 ){
            model.addAttribute("count", imageRepository.count());
        }
        else {
            model.addAttribute("count", 0);
        }

        if (imageRepository.findAll().size() > id) {
            model.addAttribute("next", (id + 1));
        }

        if (id > 1)  {
            model.addAttribute("previous", (id - 1));
        }

        model.addAttribute("current", id);
        return "/images";

    }

    @GetMapping(path = "images/{id}/content", produces = "image/jpeg")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return imageRepository.getOne(id).getContent();
    }

    @PostMapping("/images")
    public String save(@RequestParam("file") MultipartFile file) throws IOException {

        // accept only .jpeg
        if (file.getContentType().equals("image/jpeg")) {
            Image i = new Image();
            i.setContent(file.getBytes());
            imageRepository.save(i);
        }

        return "redirect:/images";


    }

}

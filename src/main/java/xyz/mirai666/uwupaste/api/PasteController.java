package xyz.mirai666.uwupaste.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.mirai666.uwupaste.PasteRepository;
import xyz.mirai666.uwupaste.UserRepository;
import xyz.mirai666.uwupaste.model.Paste;
import xyz.mirai666.uwupaste.dto.PasteDto;
import xyz.mirai666.uwupaste.model.User;

@RestController
public class PasteController {
    @Autowired
    private PasteRepository pasteRepo;
    @Autowired
    private UserRepository userRepo;

    @PostMapping("/paste")
    Paste postPaste(@RequestBody PasteDto payload, HttpServletRequest request) {
        String title = payload.title().isBlank() ? "Untitled" : payload.title();
        Paste paste = new Paste(title, payload.text(), payload.lang());
        User user = this.userRepo.findByUsername(request.getUserPrincipal().getName());
        if (user != null) {
            paste.setUploaderUsername(user.getUsername());
        }
        System.out.println(paste.getId());
        this.pasteRepo.save(paste);
        return paste;
    }
}

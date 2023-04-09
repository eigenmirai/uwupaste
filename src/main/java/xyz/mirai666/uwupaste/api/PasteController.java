package xyz.mirai666.uwupaste.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.mirai666.uwupaste.PasteRepository;
import xyz.mirai666.uwupaste.UserRepository;
import xyz.mirai666.uwupaste.model.Paste;
import xyz.mirai666.uwupaste.dto.PasteDto;
import xyz.mirai666.uwupaste.model.User;

@RestController
@RequiredArgsConstructor
public class PasteController {
    private final PasteRepository pasteRepo;
    private final UserRepository userRepo;

    @PostMapping("/paste")
    Paste postPaste(@RequestBody PasteDto payload, Authentication authentication) {
        String title = payload.title().isBlank() ? "Untitled" : payload.title();
        Paste paste = new Paste(title, payload.text(), payload.lang());
        if (authentication != null) {
            User user = this.userRepo.findByUsername(authentication.getName());
            paste.setUploader(user);
            user.addPaste(paste);
        }
        System.out.println(paste.getId());
        this.pasteRepo.save(paste);
        return paste;
    }
}

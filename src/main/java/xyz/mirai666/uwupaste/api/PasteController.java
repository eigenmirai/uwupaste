package xyz.mirai666.uwupaste.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.mirai666.uwupaste.repository.PasteRepository;
import xyz.mirai666.uwupaste.repository.UserRepository;
import xyz.mirai666.uwupaste.model.entity.Paste;
import xyz.mirai666.uwupaste.model.dto.PasteDto;
import xyz.mirai666.uwupaste.model.entity.User;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PasteController {
    private final PasteRepository pasteRepo;
    private final UserRepository userRepo;

    @PostMapping("/paste")
    public Paste postPaste(@RequestBody PasteDto payload, Authentication authentication) {
        String title = payload.title().isBlank() ? "Untitled" : payload.title();
        Paste paste = new Paste(title, payload.text(), payload.lang());
        if (authentication != null) {
            User user = this.userRepo.findByUsername(authentication.getName());
            paste.setUploader(user);
            user.addPaste(paste);
        }
        log.info("Added paste to database: {}", paste);
        this.pasteRepo.save(paste);
        return paste;
    }
}

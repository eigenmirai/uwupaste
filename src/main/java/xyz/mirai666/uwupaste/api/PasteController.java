package xyz.mirai666.uwupaste.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.mirai666.uwupaste.PasteRepository;
import xyz.mirai666.uwupaste.model.Paste;
import xyz.mirai666.uwupaste.dto.PasteDto;

@RestController
public class PasteController {
    @Autowired
    private PasteRepository repo;

    @PostMapping("/paste")
    Paste postPaste(@RequestBody PasteDto payload) {
        String title = payload.title().isBlank() ? "Untitled" : payload.title();
        Paste paste = new Paste(title, payload.text(), payload.lang());
        System.out.println(paste.getId());
        this.repo.save(paste);
        return paste;
    }
}

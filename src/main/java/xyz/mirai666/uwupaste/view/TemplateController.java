package xyz.mirai666.uwupaste.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.mirai666.uwupaste.PasteRepository;
import xyz.mirai666.uwupaste.dto.StatsDto;
import xyz.mirai666.uwupaste.model.Language;
import xyz.mirai666.uwupaste.util.Util;
import xyz.mirai666.uwupaste.model.Paste;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.StreamSupport;

@Controller
public class TemplateController {
    private PasteRepository repo;

    @Autowired
    public TemplateController(PasteRepository repo) {
        this.repo = repo;
        Util.saveTestPastes(repo);
    }

    @GetMapping({"/", "/home"})
    public String getHomeView(Model model) {
        AtomicLong lines = new AtomicLong();
        this.repo.findAll().forEach(e -> lines.addAndGet(e.getText().lines().count()));
        model.addAttribute("lines", lines.toString());
        model.addAttribute("pasteN", this.repo.count());

        List<Paste> latestPastes = StreamSupport.stream(this.repo.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Paste::getTimestamp).reversed())
                .limit(6) // show 5 entries
                .toList();
        model.addAttribute("latest", latestPastes);
        model.addAttribute("latestN", latestPastes.size());
        model.addAttribute("examples", Util.examplePastes);
        return "home";
    }

    @GetMapping("/upload")
    public String getUploadView(Model model) {
        List<Language> languages = Language.languageMap.values().stream().toList();
        model.addAttribute("languages", languages);
        return "upload";
    }

    @GetMapping("/paste/{id}")
    public String getPasteView(Model model, @PathVariable String id) {
        Optional<Paste> opt = this.repo.findById(id);
        if (opt.isEmpty()) { // paste not found
            return "error";
        }
        Paste paste = opt.get();
        model.addAttribute("id", paste.getId());
        model.addAttribute("title", paste.getTitle());
        model.addAttribute("text", paste.getText());
        model.addAttribute("lang", Language.languageMap.get(paste.getLang()));
        model.addAttribute("size", String.valueOf((float) paste.getBytes()/1024).substring(0, 4) + "KB");
        model.addAttribute("timestamp", paste.formatTimestamp());

        List<Paste> latestPastes = StreamSupport.stream(this.repo.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Paste::getTimestamp).reversed())
                .limit(6) // show 5 entries
                .toList();
        model.addAttribute("latest", latestPastes);
        model.addAttribute("latestN", latestPastes.size());
        return "paste";
    }

    @GetMapping("/stats")
    public String getStatsView(Model model) {
        int amount = (int) this.repo.count();
        model.addAttribute("amount", amount);

        // get all pastes and sort them by date added (reversed)
        List<Paste> latest = StreamSupport.stream(this.repo.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Paste::getTimestamp).reversed())
                .limit(10) // don't show all entries
                .toList();
        model.addAttribute("latest", latest);
        model.addAttribute("latestN", latest.size());

        // compute language distribution
        Map<String, Integer> distributionAbs = new HashMap<>();
        for (Paste paste : this.repo.findAll()) {
            if (distributionAbs.containsKey(paste.getLang())) {
                int c = distributionAbs.get(paste.getLang());
                distributionAbs.put(paste.getLang(), c + 1);
            } else {
                distributionAbs.put(paste.getLang(), 1);
            }
        }
        List<StatsDto> distribution = new ArrayList<>();
        distributionAbs.forEach((k, v) -> distribution.add(new StatsDto(Language.languageMap.get(k), (v.floatValue()/(float)amount)*100f)));
        distribution.sort(Comparator.comparing(StatsDto::percentage).reversed());
        model.addAttribute("distribution", distribution);
        return "stats";
    }

    @GetMapping("/error")
    public String getErrorView(Model model, HttpServletRequest request) {
        String code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString();
        model.addAttribute("code", code);
        return "error";
    }
}
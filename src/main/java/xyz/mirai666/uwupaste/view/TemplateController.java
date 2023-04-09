package xyz.mirai666.uwupaste.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.mirai666.uwupaste.PasteRepository;
import xyz.mirai666.uwupaste.UserRepository;
import xyz.mirai666.uwupaste.dto.StatsDto;
import xyz.mirai666.uwupaste.model.HttpStatusCode;
import xyz.mirai666.uwupaste.model.Language;
import xyz.mirai666.uwupaste.model.User;
import xyz.mirai666.uwupaste.util.Util;
import xyz.mirai666.uwupaste.model.Paste;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.StreamSupport;

@Controller
public class TemplateController implements ErrorController {
    private PasteRepository pasteRepo;
    private UserRepository userRepo;
    public static Map<String, HttpStatusCode> httpStatusCodeMap;

    static {
        String statusCodeListJsonUrl = "https://raw.githubusercontent.com/anonkey/http-status-code-json/master/index.json";
        try (Scanner scanner = new Scanner(new URL(statusCodeListJsonUrl).openStream(), StandardCharsets.UTF_8)) {
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                String json = scanner.next();
                ObjectMapper mapper = new ObjectMapper();
                httpStatusCodeMap = mapper.readValue(json, new TypeReference<>() {});
            }
        } catch (IOException e) {
            Util.logColored(Level.ERROR, Color.red, "Failed to fetch HTTP error code list, error handling will fail. (%s)", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Autowired
    public TemplateController(PasteRepository pasteRepo, UserRepository userRepo) {
        this.pasteRepo = pasteRepo;
        this.userRepo = userRepo;
        Util.saveTestPastes(pasteRepo);
    }

    @GetMapping({"/", "/home"})
    public String getHomeView(Model model) {
        AtomicLong lines = new AtomicLong();
        this.pasteRepo.findAll().forEach(e -> lines.addAndGet(e.getText().lines().count()));
        model.addAttribute("lines", lines.toString());
        model.addAttribute("pasteN", this.pasteRepo.count());

        List<Paste> latestPastes = StreamSupport.stream(this.pasteRepo.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Paste::getTimestamp).reversed())
                .limit(6) // show 6 entries
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
        Optional<Paste> opt = this.pasteRepo.findById(id);
        if (opt.isEmpty()) { // paste not found
            HttpStatusCode status = httpStatusCodeMap.get("404");
            model.addAttribute("code", status.getCode());
            model.addAttribute("message", status.getMessage());
            model.addAttribute("description", status.getDescription());
            return "error";
        }
        Paste paste = opt.get();
        model.addAttribute("id", paste.getId());
        model.addAttribute("title", paste.getTitle());
        model.addAttribute("text", paste.getText());
        model.addAttribute("lang", Language.languageMap.get(paste.getLang()));
        model.addAttribute("size", String.valueOf((float) paste.getBytes()/1024).substring(0, 4) + "KB");
        model.addAttribute("timestamp", paste.formatTimestamp());

        List<Paste> latestPastes = StreamSupport.stream(this.pasteRepo.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Paste::getTimestamp).reversed())
                .limit(6) // show 5 entries
                .toList();
        model.addAttribute("latest", latestPastes);
        model.addAttribute("latestN", latestPastes.size());
        return "paste";
    }

    @GetMapping("/stats")
    public String getStatsView(Model model) {
        int amount = (int) this.pasteRepo.count();
        model.addAttribute("amount", amount);

        // get all pastes and sort them by date added (reversed)
        List<Paste> latest = StreamSupport.stream(this.pasteRepo.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Paste::getTimestamp).reversed())
                .limit(10) // don't show all entries
                .toList();
        model.addAttribute("latest", latest);
        model.addAttribute("latestN", latest.size());

        // compute language distribution
        Map<String, Integer> distributionAbs = new HashMap<>();
        for (Paste paste : this.pasteRepo.findAll()) {
            if (distributionAbs.containsKey(paste.getLang())) {
                int c = distributionAbs.get(paste.getLang());
                distributionAbs.put(paste.getLang(), c + 1);
            } else {
                distributionAbs.put(paste.getLang(), 1);
            }
        }
        List<StatsDto> distribution = new ArrayList<>();
        Function<Integer, Float> calc = (x) -> Math.round((x/(float)amount)*100f * 100f) / 100f;
        distributionAbs.forEach((k, v) -> distribution.add(new StatsDto(Language.languageMap.get(k), calc.apply(v))));
        distribution.sort(Comparator.comparing(StatsDto::percentage).reversed());
        model.addAttribute("distribution", distribution);
        return "stats";
    }

    @GetMapping("/profile")
    public String getOwnProfileView(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "redirect:/login";
        }
        return "redirect:/profile/" + principal.getName();
    }

    @GetMapping("/profile/{username}")
    public String getProfileView(Model model, @PathVariable String username) {
        User user = this.userRepo.findByUsername(username);
        if (user == null) {
            HttpStatusCode status = httpStatusCodeMap.get("404");
            model.addAttribute("code", status.getCode());
            model.addAttribute("message", status.getMessage());
            model.addAttribute("description", status.getDescription());
            return "error";
        }
        model.addAttribute("user", user);
        model.addAttribute("pastes", user.getPastes());
        model.addAttribute("pasteN", user.getPastes().size());
        return "profile";
    }

    @RequestMapping("/error")
    public String getErrorView(Model model, HttpServletRequest request) {
        HttpStatusCode status = httpStatusCodeMap.get(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
        model.addAttribute("code", status.getCode());
        model.addAttribute("message", status.getMessage());
        model.addAttribute("description", status.getDescription());
        return "error";
    }
}

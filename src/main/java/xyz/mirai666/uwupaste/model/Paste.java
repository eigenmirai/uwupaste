package xyz.mirai666.uwupaste.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Paste {
    static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault());;

    @Id
    private String id;
    private String title;
    @Column(length = 1048576)
    private String text;
    private String lang;
    private int bytes;
    private Instant timestamp;
    private String uploaderUsername;

    public Paste(String title, String text, String lang) {
        this(UUID.randomUUID().toString(), title, text, lang, text.getBytes().length, Instant.now(), "guest");
    }

    public Paste(String id, String title, String text, String lang, Instant timestamp) {
        this(id, title, text, lang, text.getBytes().length, timestamp, "guest");
    }

    public String getPreview() {
        return this.text.substring(0, Math.min(this.text.length(), 147)).concat("...");
    }

    public String formatTimestamp() {
        return fmt.format(this.timestamp);
    }

    public long when() {
        return Instant.now().getEpochSecond() - this.timestamp.getEpochSecond();
    }

    public Language language() {
        return Language.languageMap.get(this.lang);
    }
}

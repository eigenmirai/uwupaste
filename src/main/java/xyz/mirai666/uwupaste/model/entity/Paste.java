package xyz.mirai666.uwupaste.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import xyz.mirai666.uwupaste.model.domain.Language;

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
    // https://gist.github.com/danthe1st/a7cab8cad5a3044b3562e4f83530c12c
    @ManyToOne
    @JoinColumn(name = "uploader")
    // this is important, because User references this class too, so there will be infinite recursion when jackson tries to serialize either
    // see https://stackoverflow.com/a/18288939
    @JsonBackReference
    private User uploader;

    public Paste(String title, String text, String lang) {
        this(UUID.randomUUID().toString(), title, text, lang, text.getBytes().length, Instant.now(), User.anon);
    }

    public Paste(String id, String title, String text, String lang, Instant timestamp) {
        this(id, title, text, lang, text.getBytes().length, timestamp, User.anon);
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

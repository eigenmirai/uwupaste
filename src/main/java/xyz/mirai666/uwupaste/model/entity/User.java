package xyz.mirai666.uwupaste.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users") // this is important because "user" is a reserved keyword in H2 sql, so there will be a syntax error
@Getter
@Setter
@ToString(exclude = "pastes")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public static User anon = new User("anonymous", "", "");

    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private Instant created;
    @Column(length = 10000)
    @OneToMany(mappedBy = "uploader")
    @JsonManagedReference // https://stackoverflow.com/a/18288939
    private Set<Paste> pastes = new HashSet<>();

    public User(String username, String email, String password) {
        this(UUID.randomUUID().toString(), username, email, password, Instant.now(), new HashSet<>());
    }
    public void addPaste(Paste s) {
        this.pastes.add(s);
    }
}

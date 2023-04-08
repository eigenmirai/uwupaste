package xyz.mirai666.uwupaste.model;

import jakarta.persistence.*;
import lombok.*;
import xyz.mirai666.uwupaste.util.StringListConverter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users") // this is important because "user" is a reserved keyword in H2 sql, so there will be a syntax error
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private Instant created;
    @Column(length = 10000)
    @Convert(converter = StringListConverter.class)
    private List<String> pastes; // paste ids

    public User(String username, String email, String password) {
        this(UUID.randomUUID().toString(), username, email, password, Instant.now(), new ArrayList<>());
    }
}

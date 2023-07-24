package xyz.mirai666.uwupaste.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.mirai666.uwupaste.repository.UserRepository;
import xyz.mirai666.uwupaste.model.entity.User;

@RestController
public class UserController {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      @RequestParam("email") String email) {
        if ("guest".equals(username) || "anonymous".equals(username)) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
        if (this.repo.findByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:8080/login?usernameExists") // fix this
                    .build();
        }
        if (this.repo.findByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:8080/login?emailExists")
                    .build();
        }
        this.repo.save(new User(username, email, this.encoder.encode(password)));
        return ResponseEntity.ok().build();
    }
}

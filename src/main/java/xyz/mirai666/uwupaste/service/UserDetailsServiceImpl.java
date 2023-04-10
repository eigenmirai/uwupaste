package xyz.mirai666.uwupaste.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.mirai666.uwupaste.repository.UserRepository;
import xyz.mirai666.uwupaste.model.domain.UserDetailsImpl;
import xyz.mirai666.uwupaste.model.entity.User;

import java.util.Optional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository repo;
    private PasswordEncoder encoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
        createUsers();
    }

    void createUsers() {
        this.repo.save(new User("user", "email@email.com", this.encoder.encode("password")));
        this.repo.save(User.anon);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  Optional.ofNullable(this.repo.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
        log.info("User found in the database: {}", user);
        return new UserDetailsImpl(user);

    }
}

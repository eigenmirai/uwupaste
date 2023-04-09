package xyz.mirai666.uwupaste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.mirai666.uwupaste.UserRepository;
import xyz.mirai666.uwupaste.model.CustomUserDetails;
import xyz.mirai666.uwupaste.model.User;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository repo;
    private PasswordEncoder encoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.repo.save(new User("user", "email@email.com", encoder.encode("password")));
        this.repo.save(User.anon);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(this.repo.findByUsername(username))
                .map(e -> {System.out.println(e); return e;})
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}

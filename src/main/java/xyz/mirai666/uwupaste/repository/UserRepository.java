package xyz.mirai666.uwupaste.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.mirai666.uwupaste.model.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email);
}

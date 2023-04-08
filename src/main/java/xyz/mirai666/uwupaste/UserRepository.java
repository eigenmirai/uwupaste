package xyz.mirai666.uwupaste;

import org.springframework.data.repository.CrudRepository;
import xyz.mirai666.uwupaste.model.User;

public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email);
}

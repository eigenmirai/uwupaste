package xyz.mirai666.uwupaste.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import xyz.mirai666.uwupaste.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserRepository repo;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSec) throws Exception {
        httpSec
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/", "/home", "/paste", "/paste/**", "/upload", "/assets/**", "/stats", "/error", "/register", "/profile/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .formLogin(login ->
                        login.loginPage("/login")
                                .defaultSuccessUrl("/home", true)
                                .failureUrl("/login?error")
                                .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/home")
                        .permitAll());
        httpSec.csrf().disable();
        return httpSec.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

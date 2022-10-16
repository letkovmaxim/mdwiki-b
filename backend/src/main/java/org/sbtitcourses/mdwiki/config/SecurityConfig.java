package org.sbtitcourses.mdwiki.config;

import org.sbtitcourses.mdwiki.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Настройка Spring Security
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    /**
     * Сервис загрузки пользователя
     */
    private final PersonDetailsService personDetailsService;


    /**
     * Инициализация поля
     */
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    /**
     * Данный метод выполняет следующие функции
     *  Настройка аутентификации
     *  Настройка авторизации
     *  Настройка формы для входа
     *  Удаляет сессии и куки
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(personDetailsService).passwordEncoder(passwordEncoder());

        http
                .authorizeRequests()
                    .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                    .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/process_login")
                    .defaultSuccessUrl("/main", true)
                    .failureUrl("/auth/login?error")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/auth/login");

        return http.build();
    }

    /**
     * Шифровальщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

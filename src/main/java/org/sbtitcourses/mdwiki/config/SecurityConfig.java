package org.sbtitcourses.mdwiki.config;

import org.sbtitcourses.mdwiki.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //Стандартная аунтификация по юзернейму и паролю
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    //Настройка самого Spring Security
    //Настройка авторизации
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Правила читаются сверху вниз
        http

                //Настройка авторизации
                .authorizeRequests()
//                    .antMatchers("/admin").hasRole("ADMIN")
                    .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                    .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                //Настройка формы для входа
                .formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/process_login")
                    .defaultSuccessUrl("/hello", true)
                    .failureUrl("/auth/login?error")
                .and()
                //Удаляется сессия и куки
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/auth/login");
    }

    //Настройка аунтификации
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(personDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    //Шифровальщик паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

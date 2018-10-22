package com.karnavauli.app.security;

import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class MyWebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;

    public MyWebSecurity(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        //czytanie z bazy uzytkownikow
        for(int i=0; i<userService.getAll().size(); i++){
//            UserDto userDtos = userService.getOneUser((long) i).get();
            UserDto us = userService.getAll().get(i);
            UserDto userDto = userService.getOneUser(us.getId()).get();
            auth
                    .inMemoryAuthentication()
                    .withUser(userDto.getUsername()).password(userDto.getPassword()).roles(String.valueOf(userDto.getRole()));
        }

        //wpisywanie na sztywno uzytkownikow
        /*auth
                .inMemoryAuthentication()
                .withUser("user").password("1234").roles(String.valueOf(Role.USER))
                .and()
                .withUser("admin").password("1234").roles(String.valueOf(Role.ADMIN))
                .and()
                .withUser("admin_lazarski").password("1234").roles(String.valueOf(Role.LAZARSKI_ADMIN))
                .and()
                .withUser("admin_sggw").password("1234").roles(String.valueOf(Role.SGGW_ADMIN))
                .and()
                .withUser("admin_pw").password("1234").roles(String.valueOf(Role.PW_ADMIN));*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                /*.antMatchers("/user").hasAnyRole(String.valueOf(Role.CEO), String.valueOf(Role.PW_ADMIN),
                   String.valueOf(Role.USER)) //strona dla kazdego
                .antMatchers("/admin").hasAnyRole(String.valueOf(Role.CEO)) //strona tylko dla CEO
                .antMatchers("/testUczelnie").hasAnyRole(String.valueOf(Role.CEO), String.valueOf(Role.PW_ADMIN))
                //strona dla uczlenianych adminow*/
                .antMatchers("/register").hasAnyRole(String.valueOf(Role.CEO)) // strona tylko dla CEO
                .antMatchers("/table/update/{id}").hasAnyRole(String.valueOf(Role.CEO))
                .antMatchers("/ticket/update/{id}").hasAnyRole(String.valueOf(Role.CEO))
                .antMatchers("/ticket/remove/{id}").hasAnyRole(String.valueOf(Role.CEO))
                .antMatchers("/addTicket").hasAnyRole(String.valueOf(Role.CEO))
                .anyRequest().authenticated() // pozostale zadania maja byc objete logowaniem

                    //strona addCustomer jest dla kazdego i to mowi powyzsza linijka
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/app-login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureUrl("/login/error")

                .and()
                .logout().permitAll()
                .logoutUrl("/app-logout")
                .clearAuthentication(true)
                .logoutSuccessUrl("/login")

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())

                .and()
                .httpBasic();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletResponse.sendRedirect("/accessDenied");
            }
        };
    }

    @Bean
    public NoOpPasswordEncoder noOpPasswordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}

package com.mindhub.homebanking.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebAuthorization {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/web/css/**", "/web/img/**", "/web/js/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/**").hasAuthority("USER");

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.formLogin()
                .loginPage("/api/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
                .successHandler((req, res, auth) -> {
                    clearAuthenticationAttributes(req);
                    //req.getSession().setAttribute("message", "Login successful");
                })
                .failureHandler((req, res, exc) -> {
                    // req.getSession().setAttribute("message", "Login failed");
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                });

        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> {
            res.sendRedirect("/web/index.html");
        });

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}



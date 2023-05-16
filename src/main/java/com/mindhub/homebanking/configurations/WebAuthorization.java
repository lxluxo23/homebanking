package com.mindhub.homebanking.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .antMatchers("/web/index.html").permitAll()
                        .antMatchers("/api/login/**").permitAll()
                        .antMatchers("/web/css/**", "/web/img/**", "/web/js/**").permitAll()
                        .antMatchers("/actuator/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                        .antMatchers("/swagger-ui.html").permitAll()
                        .antMatchers("/swagger-ui/**").permitAll()
                        .antMatchers("/v3/api-docs").permitAll()
                        .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                        .antMatchers("/admin/**").hasAuthority("ADMIN")
                        .antMatchers("/rest/**").hasAuthority("ADMIN")
                        .antMatchers("/**").hasAuthority("USER"))
                .headers(headers -> headers
                        .frameOptions().disable())
                .formLogin(formLogin -> formLogin
                        .loginPage("/api/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll()
                        .successHandler((req, res, auth) -> {
                            Map<String, Object> successResponse = new HashMap<>();
                            successResponse.put("message", "Login successful");
                            res.setStatus(HttpServletResponse.SC_OK);
                            res.setContentType("application/json");
                            res.getWriter().write(new ObjectMapper().writeValueAsString(successResponse));
                            clearAuthenticationAttributes(req);
                        })
                        .failureHandler((req, res, exc) -> {
                            Map<String, Object> errorResponse = new HashMap<>();
                            errorResponse.put("message", "Login failed");
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                        }))
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((req, res, exc) -> {
                            res.sendRedirect("/web/index.html");
                        }));

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

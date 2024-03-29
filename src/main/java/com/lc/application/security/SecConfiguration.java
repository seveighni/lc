package com.lc.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("/register/**").permitAll()
                        .requestMatchers("/profile").authenticated()
                        .requestMatchers(HttpMethod.GET, "/offices").hasAnyAuthority("ADMIN", "EMPLOYEE", "CUSTOMER")
                        .requestMatchers("/offices").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/offices/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/users/**").hasAuthority("ADMIN")
                        .requestMatchers("/employees/**").hasAuthority("ADMIN")
                        .requestMatchers("/rates/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/customers/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/parcels").hasAnyAuthority("ADMIN", "EMPLOYEE", "CUSTOMER")
                        .requestMatchers("/parcels").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/parcels/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/home").authenticated())
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/home", true)
                                .permitAll())
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll())
                .exceptionHandling(e -> e.accessDeniedHandler(new RedirectAccessDeniedHandler()));
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
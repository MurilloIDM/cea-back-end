package com.cea.security;

import com.cea.security.administrator.JWTAuthenticateFilterAdministrator;
import com.cea.security.administrator.JWTValidateFilterAdministrator;
import com.cea.security.student.JWTAuthenticateFilterStudent;
import com.cea.security.student.JWTValidateFilterStudent;
import com.cea.services.AdministratorDetailsService;
import com.cea.services.StudentDetailsService;
import com.cea.utils.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class JWTConfigure {

    @Configuration
    @Order(1)
    @RequiredArgsConstructor
    public static class JWTConfigureStudent extends WebSecurityConfigurerAdapter {

        private final ConfigProperties configProperties;
        private final StudentDetailsService studentDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            auth.userDetailsService(studentDetailsService).passwordEncoder(encoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().cors().and()
                    .antMatcher("/app/**")
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/app/login").permitAll()
                    .antMatchers("/app/**").authenticated()
                    .and()
                    .addFilter(new JWTAuthenticateFilterStudent(authenticationManager(), configProperties))
                    .addFilter(new JWTValidateFilterStudent(authenticationManager(), configProperties))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

    }

    @Configuration
    @Order(2)
    @RequiredArgsConstructor
    public static class JWTConfigureAdministrator extends WebSecurityConfigurerAdapter {

        private final ConfigProperties configProperties;
        private final AdministratorDetailsService administratorDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            auth.userDetailsService(administratorDetailsService).passwordEncoder(encoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .antMatcher("/admin/**").cors().and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/admin/login").permitAll()
                    .antMatchers("/admin/**").authenticated()
                    .and()
                    .addFilter(new JWTAuthenticateFilterAdministrator(authenticationManager(), configProperties))
                    .addFilter(new JWTValidateFilterAdministrator(authenticationManager(), configProperties))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

    }

}

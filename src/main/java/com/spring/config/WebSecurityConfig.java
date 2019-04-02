package com.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    Filter keyFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable().addFilterAfter(keyFilter, BasicAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers( "/location", "/ui/index").permitAll()
                .antMatchers(HttpMethod.POST, "/install").permitAll()
            .anyRequest().authenticated();
    }
}

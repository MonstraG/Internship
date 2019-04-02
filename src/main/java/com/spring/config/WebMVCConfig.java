package com.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Config for Spring MVC and Thymeleaf
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.spring"})
public class WebMVCConfig implements WebMvcConfigurer { }

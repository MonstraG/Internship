package com.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Config for WebMVC, while there isn't anything inside, it's existence and the fact that it implements
 * WebMvcConfigurer allows Spring MVC to work.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.spring"})
public class WebMVCConfig implements WebMvcConfigurer {
}

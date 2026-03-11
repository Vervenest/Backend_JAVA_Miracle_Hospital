package com.miracle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfig
 * JSP view prefix/suffix configured in application.yml:
 *   spring.mvc.view.prefix=/WEB-INF/views/
 *   spring.mvc.view.suffix=.jsp
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // intentionally empty - WAR packaging + application.yml handles everything
}
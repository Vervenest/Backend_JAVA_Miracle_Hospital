package com.miracle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig - Static resource handling.
 * Files in src/main/webapp/ (assets/, WEB-INF/) are served automatically
 * by WAR container. Only classpath:/static/ files need explicit mapping.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/index.html")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);

        registry.addResourceHandler("/app.js")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/assets/doctorProfileImage/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/webapp/assets/doctorProfileImage/")
                .setCachePeriod(3600);     
                
        registry.addResourceHandler("/assets/patientDocuments/**")
        .addResourceLocations("file:" + System.getProperty("user.dir") + "/assets/patientDocuments/");
    }
}
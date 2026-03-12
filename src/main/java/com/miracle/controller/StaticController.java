package com.miracle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * StaticController - Serves static frontend files
 * Directly serves index.html and app.js from classpath resources
 */
@RestController
public class StaticController {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Serve index.html at root path
     */
   // ✅ NEW
@GetMapping("/")
public RedirectView root() {
    return new RedirectView("/admin/login");
}
    /**
     * Serve index.html directly
     */
    @GetMapping("/index.html")
    public ResponseEntity<String> indexHtml() throws IOException {
        String content = getResourceContent("classpath:static/index.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
                .body(content);
    }

    /**
     * Serve app.js
     */
    @GetMapping("/app.js")
    public ResponseEntity<String> appJs() throws IOException {
        String content = getResourceContent("classpath:static/app.js");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .body(content);
    }

    

    private String getResourceContent(String resourcePath) throws IOException {
        org.springframework.core.io.Resource resource = resourceLoader.getResource(resourcePath);
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}

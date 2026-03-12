package com.miracle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class StaticController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/")
    public RedirectView root() {
        return new RedirectView("/admin/login");
    }

    @ResponseBody
    @GetMapping("/index.html")
    public ResponseEntity<String> indexHtml() throws IOException {
        String content = getResourceContent("classpath:static/index.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
                .body(content);
    }

    @ResponseBody
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
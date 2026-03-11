package com.miracle.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminLoginController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/admin/login")
    public String adminLogin(HttpSession session) {
        // If already logged in, go straight to dashboard
        if (session.getAttribute("adminEmail") != null) {
            return "redirect:/admin/dashboard";
        }
        return "admin/adminLogin";
    }

    @PostMapping("/adminmodel/loginemail")
    public String loginEmail(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {
        try {
            // Query adminusers table directly — plain password like original PHP
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT * FROM adminusers WHERE adminEmail = ? AND adminPassword = ? AND adminStatus = '1'",
                email, password
            );

            if (!rows.isEmpty()) {
                Map<String, Object> admin = rows.get(0);
                session.setAttribute("adminEmail", admin.get("adminEmail"));
                session.setAttribute("adminId", admin.get("adminId"));
                session.setAttribute("adminName", admin.get("adminName"));
                log.info("Admin login success: {}", email);
                return "redirect:/admin/dashboard";
            } else {
                log.warn("Admin login failed for: {}", email);
                model.addAttribute("errorMessage", "Invalid email or password");
                return "admin/adminLogin";
            }

        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            model.addAttribute("errorMessage", "Login failed. Please try again.");
            return "admin/adminLogin";
        }
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
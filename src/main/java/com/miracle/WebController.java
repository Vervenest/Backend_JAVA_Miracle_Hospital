package com.miracle;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"/app", "/app/**"})
    public String catchAll(HttpServletRequest req, Model model) {
        String uri = req.getRequestURI();
        if (uri == null || uri.isEmpty()) {
            return "redirect:/admin/login";
        }
        if (uri.startsWith("/")) uri = uri.substring(1);
        int q = uri.indexOf('?');
        if (q >= 0) uri = uri.substring(0, q);
        if (uri.endsWith("/")) uri = uri.substring(0, uri.length() - 1);
        model.addAttribute("_sampleAttr", "sampleValue");
        return uri;
    }
}
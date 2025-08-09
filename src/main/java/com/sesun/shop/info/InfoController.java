package com.sesun.shop.info;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InfoController {
    private final InfoRepository infoRepository;
    @GetMapping("/info")
    String info(Model model) {
        List<Info> result = infoRepository.findAll();
        model.addAttribute("info", result);
        return "info.html";
    }

}

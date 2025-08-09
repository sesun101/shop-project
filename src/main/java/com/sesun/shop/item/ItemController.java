package com.sesun.shop.item;

import com.sun.net.httpserver.HttpsServer;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final S3Service s3Service;
    @GetMapping("/list/page/{pageNum}")
    String listPage(@PathVariable Integer pageNum, Model model, HttpSession session) {
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(pageNum-1, 5));
        session.setAttribute("currentPage", pageNum);
        model.addAttribute("items", result);
        model.addAttribute("totalPages", result.getTotalPages());
        return "list.html";
    }
    @GetMapping("/list")
    String list(Model model) {
        return "redirect:/list/page/1";
    }
    @GetMapping("/write")
    String write() {
        return "write.html";
    }
    @PostMapping("/add")
    String addPost(String title, Integer price, String username, String imgURL) {
        itemService.saveItem(title, price, username, imgURL);
        return "redirect:/list";
    }
    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("data", result.get());
            return "detail.html";
        }
        else {
            return "error.html";
        }
    }
    @GetMapping("/edit/{id}")
    String showEdit(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("data", result.get());
            return "edit.html";
        }
        else {
            return "redirect:/list";
        }
    }
    @PostMapping("/edit")
    String edit(Long id, String newTitle, Integer newPrice, String newUsername, String newImgURL, HttpSession session) {
        itemService.updateItem(id, newTitle, newPrice, newUsername, newImgURL);
        int page = (int) session.getAttribute("currentPage");
        return "redirect:/list/page/" + page;
    }
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    ResponseEntity<String> deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
        return ResponseEntity.ok("삭제완료");
    }
    @GetMapping("/test2")
    String test2() {
        var result = new BCryptPasswordEncoder().encode("asdasdasdasd");
        System.out.println(result);
        return "redirect:/list";
    }
    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String fileName) {
        String result = s3Service.createPresignedUrl("test/" + fileName);
        return result;
    }
    @PostMapping("/delete/image/{id}")
    String deleteImage(@PathVariable Long id, HttpSession session) {
        s3Service.deleteItemImage(id);
        int page = (int) session.getAttribute("currentPage");
        return "redirect:/list/page/" + page;
    }
}

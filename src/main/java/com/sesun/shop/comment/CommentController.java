package com.sesun.shop.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    @PostMapping("/comment")
        String addComment(String content, String username, Long itemId) {
        commentService.saveComment(content, username, itemId);
        return "redirect:/detail/" + itemId;
    }
}

package com.sesun.shop.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public void saveComment(String content, String username, Long itemId) {
        Comment comment = new Comment();
        comment.setComment(content);
        comment.setUsername(username);
        comment.setItemId(itemId);
        commentRepository.save(comment);
    }
}

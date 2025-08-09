package com.sesun.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    public void saveItem(String title, Integer price, String username, String imgURL) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setUsername(username);
        if (imgURL != null && !imgURL.isBlank()) {
            item.setImg_url(imgURL);
        }
        itemRepository.save(item);
    }
    public void updateItem(Long id, String newTitle, Integer newPrice, String newUsername, String newImgURL){
        Item newItem = itemRepository.findById(id).orElseThrow();
        newItem.setTitle(newTitle);
        newItem.setPrice(newPrice);
        newItem.setUsername(newUsername);
        if (newImgURL != null && !newImgURL.isBlank()) {
            newItem.setImg_url(newImgURL);
        }
        itemRepository.save(newItem);
    }
}

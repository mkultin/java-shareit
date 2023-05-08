package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item create(Item item);

    Item update(Item item);

    Item getItemById(Long itemId);

    List<Item> getItemsByOwner(Long userId);

    List<Item> search(String text);
}

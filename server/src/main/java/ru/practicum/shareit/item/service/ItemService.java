package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long ownerId);

    ItemDto update(ItemDto itemDto, Long ownerId, Long itemId);

    ItemGetDto getItemById(Long itemId, Long ownerId);

    List<ItemGetDto> getItemsByOwner(Long userId, Integer from, Integer size);

    List<ItemDto> search(String text, Integer from, Integer size);

    Item getItem(Long itemId);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long authorId);

    List<ItemDto> getItemsByRequest(Long requestId);
}

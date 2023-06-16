package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.service.Constants;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto,
                          @RequestHeader(Constants.USER_HEADER) Long ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @RequestHeader(Constants.USER_HEADER) Long ownerId,
                          @PathVariable Long itemId) {
        return itemService.update(itemDto, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemGetDto getItemById(@RequestHeader(Constants.USER_HEADER) Long ownerId, @PathVariable Long itemId) {
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemGetDto> getItemsByOwner(@RequestHeader(Constants.USER_HEADER) Long ownerId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto,
                                    @RequestHeader(Constants.USER_HEADER) Long authorId, @PathVariable Long itemId) {
        return itemService.createComment(commentDto, itemId, authorId);
    }
}

package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.service.Constants;
import ru.practicum.shareit.service.Marker;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Marker.Create.class) @RequestBody ItemDto itemDto,
                                         @RequestHeader(Constants.USER_HEADER) Long ownerId) {
        log.info("Post request: create Item by User id={}", ownerId);
        return itemClient.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto,
                                         @RequestHeader(Constants.USER_HEADER) Long ownerId,
                                         @PathVariable Long itemId) {
        log.info("Patch request: update Item id={} bby User id={}", itemId, ownerId);
        return itemClient.update(itemDto, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(Constants.USER_HEADER) Long ownerId,
                                              @PathVariable Long itemId) {
        log.info("Get request: get Item by id={} by User id={}", itemId, ownerId);
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(Constants.USER_HEADER) Long ownerId,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get request: get Items by User id={} with rom={}, size={}", ownerId, from, size);
        return itemClient.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(Constants.USER_HEADER) Long ownerId,
                                         @RequestParam String text,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get request: get Items by text={} and userId={} with from={}, size={}", text, ownerId, from, size);
        return itemClient.search(ownerId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Validated(Marker.Create.class) @RequestBody CommentDto commentDto,
                                                @RequestHeader(Constants.USER_HEADER) Long authorId,
                                                @PathVariable Long itemId) {
        log.info("Post request: create Comment for Item id={} by User id={}", itemId, authorId);
        return itemClient.createComment(commentDto, authorId, itemId);
    }
}

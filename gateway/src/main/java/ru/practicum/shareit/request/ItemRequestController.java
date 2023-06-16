package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.service.Constants;
import ru.practicum.shareit.service.Marker;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Marker.Create.class) @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(Constants.USER_HEADER) Long userId) {
        log.info("Post request: create ItemRequest");
        return itemRequestClient.create(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByRequestor(@RequestHeader(Constants.USER_HEADER) Long userId) {
        log.info("Get request: getItemRequestsByRequestor userId={}", userId);
        return itemRequestClient.getItemRequestsByRequestor(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestsById(@RequestHeader(Constants.USER_HEADER) Long userId,
                                              @PathVariable Long requestId) {
        log.info("Get request: get ItemRequest id={} by userId={}", requestId, userId);
        return itemRequestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(Constants.USER_HEADER) Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get all requests by userId={} with from={}, size={}", userId, from, size);
        return itemRequestClient.getAllRequests(userId, from, size);
    }
}

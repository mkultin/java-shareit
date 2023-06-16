package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.service.Constants;
import ru.practicum.shareit.service.Marker;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@Validated(Marker.Create.class) @RequestBody ItemRequestDto itemRequestDto,
                          @RequestHeader(Constants.USER_HEADER) Long userId) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequestor(@RequestHeader(Constants.USER_HEADER) Long userId) {
        return itemRequestService.getItemRequestsByRequestor(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestsById(@RequestHeader(Constants.USER_HEADER) Long userId,
                                              @PathVariable Long requestId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(Constants.USER_HEADER) Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAllItemRequests(userId, from, size);
    }
}

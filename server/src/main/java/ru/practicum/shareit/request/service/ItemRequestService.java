package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Long requestorId);

    List<ItemRequestDto> getItemRequestsByRequestor(Long requestorId);

    ItemRequestDto getItemRequestById(Long requestId, Long userId);

    ItemRequest getItemRequest(Long requestId);

    List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);
}

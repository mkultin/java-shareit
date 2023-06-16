package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestId) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(requestId)
                .created(LocalDateTime.now())
                .build();
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .build();
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> items) {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .build();
        itemRequestDto.getItems().addAll(items);
        return itemRequestDto;
    }
}

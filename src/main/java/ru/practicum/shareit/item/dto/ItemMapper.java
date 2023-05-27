package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }

    public Item toItem(ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .request(itemDto.getRequestId() != null ? itemDto.getRequestId() : null)
                .build();
    }

    public ItemGetDto toOwnersItemGetDto(Item item, BookingDtoForItem lastBooking, BookingDtoForItem nextBooking,
                                         List<CommentDto> itemComments) {
        return ItemGetDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(itemComments)
                .requestId(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }

    public ItemGetDto toItemGetDto(Item item, List<CommentDto> comments) {
        return ItemGetDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .requestId(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }
}

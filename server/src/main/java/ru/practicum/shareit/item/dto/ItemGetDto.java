package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingShort;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ItemGetDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingShort lastBooking;
    private BookingShort nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}

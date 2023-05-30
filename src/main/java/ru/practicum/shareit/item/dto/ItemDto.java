package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.service.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(message = "Название не может быть пустым.", groups = {Marker.Create.class})
    private String name;
    @NotBlank(message = "Описание не может быть пустым.", groups = {Marker.Create.class})
    private String description;
    @NotNull(groups = {Marker.Create.class})
    private Boolean available;
    private BookingShort lastBooking;
    private BookingShort nextBooking;
    private Long requestId;
}

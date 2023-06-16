package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.service.Marker;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "Описание не может быть пустым.", groups = {Marker.Create.class})
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private final List<ItemDto> items = new ArrayList<>();
}

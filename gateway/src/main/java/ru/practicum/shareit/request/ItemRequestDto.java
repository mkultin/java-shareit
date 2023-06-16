package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.service.Marker;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "Описание не может быть пустым.", groups = {Marker.Create.class})
    private String description;
    private Long requestorId;
    private LocalDateTime created;
}

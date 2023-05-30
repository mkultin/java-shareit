package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.service.Marker;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(message = "Комментарий не может быть пустым.", groups = {Marker.Create.class})
    private String text;
    private String authorName;
    private LocalDateTime created;
}

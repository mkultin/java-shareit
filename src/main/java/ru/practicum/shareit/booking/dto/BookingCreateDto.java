package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.service.Marker;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingCreateDto {
    @NotNull(message = "Укажите дату старта бронирования", groups = {Marker.Create.class})
    @FutureOrPresent(message = "Дата не может быть в прошлом", groups = {Marker.Create.class})
    private LocalDateTime start;
    @NotNull(message = "Укажите дату окончания бронирования", groups = {Marker.Create.class})
    @Future(message = "Дата не может быть в прошлом", groups = {Marker.Create.class})
    private LocalDateTime end;
    @NotNull(message = "Укажите вещь для бронирования", groups = {Marker.Create.class})
    private Long itemId;
}

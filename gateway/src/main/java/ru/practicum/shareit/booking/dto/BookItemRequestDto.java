package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.service.Marker;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class
BookItemRequestDto {
	@NotNull(message = "Укажите дату старта бронирования", groups = {Marker.Create.class})
	@FutureOrPresent(message = "Дата не может быть в прошлом", groups = {Marker.Create.class})
	private LocalDateTime start;
	@NotNull(message = "Укажите дату окончания бронирования", groups = {Marker.Create.class})
	@Future(message = "Дата не может быть в прошлом", groups = {Marker.Create.class})
	private LocalDateTime end;
	@NotNull(message = "Укажите вещь для бронирования", groups = {Marker.Create.class})
	private Long itemId;
}

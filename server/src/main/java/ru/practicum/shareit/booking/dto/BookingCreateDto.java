package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@Builder
public class BookingCreateDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}

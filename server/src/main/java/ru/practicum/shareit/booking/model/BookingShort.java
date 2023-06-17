package ru.practicum.shareit.booking.model;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class BookingShort {
    private Long id;
    private Long bookerId;
}

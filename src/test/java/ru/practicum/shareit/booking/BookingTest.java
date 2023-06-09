package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingTest {
    Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.of(1000, 1, 1, 1, 1, 1))
            .end(LocalDateTime.of(1000, 1, 1, 1, 1, 2))
            .status(Status.APPROVED)
            .build();

    Booking booking2 = Booking.builder()
            .id(1L)
            .start(LocalDateTime.of(1000, 1, 1, 1, 1, 1))
            .end(LocalDateTime.of(1000, 1, 1, 1, 1, 2))
            .status(Status.APPROVED)
            .build();

    @Test
    void testBookingEqualsAndHashcode() {
        assertEquals(booking2, booking);
        assertTrue(booking.hashCode() == booking2.hashCode());
    }

}

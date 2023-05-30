package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingCreateDto booking, Long bookerId);

    BookingDto approveBooking(Long bookingId, Long bookerId, Boolean approved);

    BookingDto getBookingDtoById(Long bookingId, Long bookerId);

    Booking getBookingById(Long bookingId);

    List<BookingDto> getBookingsByBooker(Long bookerId, String state);

    List<BookingDto> getBookingsForBookerItems(Long bookerId, String state);
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.service.Constants;
import ru.practicum.shareit.service.Marker;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestBody BookingCreateDto booking,
                             @RequestHeader(Constants.USER_HEADER) Long bookerId) {
        return bookingService.create(booking, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(Constants.USER_HEADER) Long bookerId, @PathVariable Long bookingId,
                              @RequestParam Boolean approved) {
        return bookingService.approveBooking(bookingId, bookerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(Constants.USER_HEADER) Long bookerId,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingDtoById(bookingId, bookerId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByBooker(@RequestHeader(Constants.USER_HEADER) Long bookerId,
                                                @RequestParam(defaultValue = "ALL",
                                                        required = false) String state,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getBookingsByBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForBookerItems(@RequestHeader(Constants.USER_HEADER) Long bookerId,
                                                     @RequestParam(defaultValue = "ALL",
                                                             required = false) String state,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getBookingsForBookerItems(bookerId, state, from, size);
    }
}

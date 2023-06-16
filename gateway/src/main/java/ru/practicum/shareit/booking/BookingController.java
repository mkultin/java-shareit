package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.service.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.service.Constants.USER_HEADER;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader(USER_HEADER) long userId,
											  @RequestParam(name = "state",
													  defaultValue = "all") String stateParam,
											  @PositiveOrZero @RequestParam(name = "from",
													  defaultValue = "0") Integer from,
											  @Positive @RequestParam(name = "size",
													  defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsForBookerItems(@RequestHeader(USER_HEADER) long userId,
											  @RequestParam(name = "state",
													  defaultValue = "all") String stateParam,
											  @PositiveOrZero @RequestParam(name = "from",
													  defaultValue = "0") Integer from,
											  @Positive @RequestParam(name = "size",
													  defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookingsForBookerItems(userId, state, from, size);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approve(@RequestHeader(USER_HEADER) long bookerId,
										  @PathVariable Long bookingId,
										  @RequestParam Boolean approved) {
		log.info("Update booking status{}, userId={}, new approved={}", bookingId, bookerId, approved);
		return bookingClient.approveBooking(bookerId, bookingId, approved);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader(USER_HEADER) long userId,
										   @Validated(Marker.Create.class)  @RequestBody BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(USER_HEADER) long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}
}

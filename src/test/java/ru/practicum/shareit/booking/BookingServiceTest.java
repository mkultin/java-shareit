package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.expections.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.Entities.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceTest {
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final BookingService bookingService;

    @BeforeEach
    void beforeEach() {
        userService.create(userDto1);
        userService.create(userDto2);
        itemService.create(itemDto1, 1L);
        itemService.create(itemDto2, 2L);
        itemService.create(itemDto3, 2L);
        bookingService.create(bookingCreateDto1, 1L);
        bookingService.approveBooking(1L, 2L, true);
    }

    @Test
    void create() {
        ItemDto itemDto4 = ItemDto.builder()
                .name("Item4")
                .description("Description4")
                .available(false)
                .build();
        BookingCreateDto bookingCreateDto3 = BookingCreateDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(2L))
                .itemId(4L)
                .build();
        itemService.create(itemDto4, 2L);

        assertThrows(NotFoundException.class, () -> bookingService.create(bookingCreateDto3, 2L));
        assertThrows(ValidationException.class, () -> bookingService.create(bookingCreateDto3, 1L));
        bookingService.create(bookingCreateDto2, 2L);

        BookingDto bookingDto = bookingService.getBookingDtoById(2L, 1L);
        assertThat(bookingDto.getId(), equalTo(2L));
        assertThat(bookingDto.getItem().getId(), equalTo(1L));
    }

    @Test

    void getBookingDtoBy() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingDtoById(1L, 3L));

        UserDto user3 = UserDto.builder()
                .name("User3")
                .email("user3@email.com")
                .build();

        userService.create(user3);

        assertThrows(NotFoundException.class, () -> bookingService.getBookingDtoById(1L, 3L));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingDtoById(2L, 1L));

        assertThat(bookingService.getBookingDtoById(1L, 1L).getId(), equalTo(1L));
        assertThat(bookingService.getBookingDtoById(1L, 2L).getId(), equalTo(1L));
    }

    @Test
    void approve() {
        assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(1L, 2L, true));
        bookingService.approveBooking(1L, 2L, false);

        assertThat(bookingService.getBookingDtoById(1L, 1L).getStatus(), equalTo(Status.REJECTED));

        assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(1L, 3L, true));
    }

    @Test
    void getBookingsByBooker() {
        assertThrows(ValidationException.class,
                () -> bookingService.getBookingsByBooker(1L, "ALL", -1, 0));
        assertThrows(ValidationException.class,
                () -> bookingService.getBookingsByBooker(1L, "ALL", 0, -1));
        assertThrows(ValidationException.class,
                () -> bookingService.getBookingsByBooker(1L, "lays", 0, 10));
        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsByBooker(3L, "lays", 0, 10));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ALL", "PAST"})
    void getBookingsByBookerAndStatesAllAndPast(String input) {
        bookings = bookingService.getBookingsByBooker(1L, input, 0, 10);

        assertThat(bookings.size(), equalTo(1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CURRENT", "FUTURE", "WAITING", "REJECTED"})
    void getBookingsByBookerAndOtherStates(String input) {
        bookings = bookingService.getBookingsByBooker(1L, input, 0, 10);

        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    void getBookingsForBookerItems() {
        assertThrows(ValidationException.class,
                () -> bookingService.getBookingsByBooker(1L, "lays", 0, 10));
        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsByBooker(3L, "lays", 0, 10));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ALL", "PAST"})
    void getBookingsForBookerItemsByStatesAllAndPast(String input) {
        bookings = bookingService.getBookingsForBookerItems(2L, input, 0, 10);

        assertThat(bookings.size(), equalTo(1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CURRENT", "FUTURE", "WAITING", "REJECTED"})
    void getBookingsForBookerItemsByOtherStates(String input) {
        bookings = bookingService.getBookingsForBookerItems(2L, input, 0, 10);

        assertThat(bookings.size(), equalTo(0));
    }
}

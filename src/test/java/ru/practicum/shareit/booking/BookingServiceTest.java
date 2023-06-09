package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    BookingService bookingService;

    UserDto user1 = UserDto.builder()
            .name("User1")
            .email("user1@email.com")
            .build();

    UserDto user2 = UserDto.builder()
            .name("User2")
            .email("user2@email.com")
            .build();

    ItemDto itemDto1 = ItemDto.builder()
            .name("Item")
            .description("Description")
            .available(true)
            .build();

    ItemDto itemDto2 = ItemDto.builder()
            .name("Item2")
            .description("Description2")
            .available(true)
            .build();

    ItemDto itemDto3 = ItemDto.builder()
            .name("Item3")
            .description("Description3")
            .available(true)
            .build();

    BookingCreateDto bookingCreateDto1 = BookingCreateDto.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusNanos(2L))
            .itemId(2L)
            .build();

    BookingCreateDto bookingCreateDto2 = BookingCreateDto.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusNanos(2L))
            .itemId(1L)
            .build();

    @BeforeEach
    void beforeEach() {
        userService.create(user1);
        userService.create(user2);
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

        List<BookingDto> bookings = bookingService.getBookingsByBooker(1L, "ALL", 0, 10);

        assertThat(bookings.size(), equalTo(1));

        bookings = bookingService.getBookingsByBooker(1L, "CURRENT", 0, 10);

        assertThat(bookings.size(), equalTo(0));

        bookings = bookingService.getBookingsByBooker(1L, "PAST", 0, 10);

        assertThat(bookings.size(), equalTo(1));

        bookings = bookingService.getBookingsByBooker(1L, "WAITING", 0, 10);

        assertThat(bookings.size(), equalTo(0));

        bookings = bookingService.getBookingsByBooker(1L, "REJECTED", 0, 10);

        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    void getBookingsForBookerItems() {
        assertThrows(ValidationException.class,
                () -> bookingService.getBookingsByBooker(1L, "lays", 0, 10));
        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsByBooker(3L, "lays", 0, 10));

        List<BookingDto> bookings = bookingService.getBookingsForBookerItems(2L, "ALL", 0, 10);

        assertThat(bookings.size(), equalTo(1));

        bookings = bookingService.getBookingsForBookerItems(2L, "CURRENT", 0, 10);

        assertThat(bookings.size(), equalTo(0));

        bookings = bookingService.getBookingsForBookerItems(2L, "PAST", 0, 10);

        assertThat(bookings.size(), equalTo(1));

        bookings = bookingService.getBookingsForBookerItems(2L, "FUTURE", 0, 10);

        assertThat(bookings.size(), equalTo(0));

        bookings = bookingService.getBookingsForBookerItems(2L, "WAITING", 0, 10);

        assertThat(bookings.size(), equalTo(0));

        bookings = bookingService.getBookingsForBookerItems(2L, "REJECTED", 0, 10);

        assertThat(bookings.size(), equalTo(0));
    }
}

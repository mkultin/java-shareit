package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.booking.enums.Status.WAITING;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTests {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;

    private Item item;

    private User user2;

    private Booking booking;

    @BeforeEach
    void init() {
        user = new User(1L, "name", "email@email.com");

        item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .build();

        user2 = new User(2L, "name2", "email2@email.com");

        booking = Booking.builder()
                .start(LocalDateTime.of(2024, 1, 1, 0, 0))
                .end(LocalDateTime.of(2024, 1, 2, 0, 0))
                .item(item)
                .booker(user2)
                .status(WAITING)
                .build();

        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);
    }

    @Test
    void findByBookerIdOrderByStartDescTest() {
        assertThat(bookingRepository
                .findByBookerIdOrderByStartDesc(user2.getId(), PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        assertThat(bookingRepository
                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(user2.getId(),
                        LocalDateTime.of(2024, 1, 1, 2, 0),
                        LocalDateTime.of(2024, 1, 1, 23, 0),
                        PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findByBookerIdAndEndBeforeOrderByStartDescTest() {
        assertThat(bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(user2.getId(),
                        LocalDateTime.of(2024, 1, 3, 23, 0),
                        PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findByBookerIdAndStartAfterOrderByStartDescTest() {
        assertThat(bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(user2.getId(),
                        LocalDateTime.of(2023, 12, 31, 23, 0),
                        PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDescTest() {
        assertThat(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(user2.getId(), WAITING,
                        PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDescTest() {
        assertThat(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(user.getId(),
                        PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        assertThat(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(user.getId(),
                        LocalDateTime.of(2024, 1, 1, 2, 0),
                        LocalDateTime.of(2024, 1, 1, 23, 0),
                        PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDescTest() {
        assertThat(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(user.getId(),
                        LocalDateTime.of(2024, 1, 3, 23, 0),
                        PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findAllByItemOwnerIdAndStartAfterOrderByStartDescTest() {
        assertThat(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(user.getId(),
                        LocalDateTime.of(2023, 12, 31, 23, 0),
                        PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDescTest() {
        assertThat(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(user.getId(),
                        WAITING, PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findFirstByItemIdAndStartBeforeAndStatusOrderByEndDescTest() {
        BookingShort bookingShort = bookingRepository
                .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(),
                        LocalDateTime.of(2024, 1, 1, 2, 0), WAITING);
        assertThatBookingIdEq1AndBookingIdEqU2Id(bookingShort);
    }

    @Test
    void findFirstByItemIdAndStartAfterAndStatusOrderByStartAscTest() {
        BookingShort bookingShort = bookingRepository
                .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(),
                        LocalDateTime.of(2023, 12, 31, 23, 0), WAITING);
        assertThatBookingIdEq1AndBookingIdEqU2Id(bookingShort);
    }

    @Test
    void findFirstByItemIdAndBookerIdAndEndBeforeAndStatusTest() {
        BookingShort bookingShort = bookingRepository
                .findFirstByItemIdAndBookerIdAndEndBeforeAndStatus(item.getId(), user2.getId(),
                        LocalDateTime.of(2024, 1, 3, 23, 0), WAITING);
        assertThatBookingIdEq1AndBookingIdEqU2Id(bookingShort);
    }

    private void assertThatBookingIdEq1AndBookingIdEqU2Id(BookingShort booking) {
        assertThat(booking.getId(), equalTo(1L));
        assertThat(booking.getBookerId(), equalTo(user2.getId()));
    }
}
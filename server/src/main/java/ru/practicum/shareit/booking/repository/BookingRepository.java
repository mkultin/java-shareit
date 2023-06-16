package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, PageRequest pageRequest);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime dateTime,
                                                                          LocalDateTime secondDateTime,
                                                                          PageRequest pageRequest);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime dateTime,
                                                             PageRequest pageRequest);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime dateTime,
                                                              PageRequest pageRequest);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime dateTime,
                                                                              LocalDateTime secondDateTime,
                                                                                PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime dateTime,
                                                                   PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime dateTime,
                                                                    PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status,
                                                                PageRequest pageRequest);

    BookingShort findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime end,
                                                                        Status status);

    BookingShort findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime start,
                                                                        Status status);

    BookingShort findFirstByItemIdAndBookerIdAndEndBeforeAndStatus(Long itemId, Long bookerId,
                                                                   LocalDateTime dateTime, Status status);
}

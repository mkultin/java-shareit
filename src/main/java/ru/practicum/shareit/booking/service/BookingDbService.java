package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.expections.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingDbService implements BookingService {
    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto create(BookingCreateDto bookingCreateDto, Long bookerId) {
        validateDate(bookingCreateDto);
        User booker = userService.getUser(bookerId);
        Item item = itemService.getItem(bookingCreateDto.getItemId());
        if (Objects.equals(item.getOwner().getId(), bookerId)) {
            throw new NotFoundException("Вещь id=" + item.getId() + " у пользователя id=" +
                    bookerId + " не найдена.");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь id=" + item.getId() + " недоступна для бронирования.");
        }
        Booking booking = repository.save(BookingMapper.toBooking(bookingCreateDto, item, booker, Status.WAITING));
        log.info("Пользователем id={} создано бронирование id={} вещи id={}",
                bookerId, booking.getId(), item.getId());
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long bookingId, Long bookerId, Boolean approved) {
        userService.getUser(bookerId);
        Booking booking = getBookingById(bookingId);
        Item item = booking.getItem();
        if (!Objects.equals(item.getOwner().getId(), bookerId)) {
            throw new NotFoundException("У пользователя id=" + bookerId
                    + " нет прав для подтверждения бронирования id=" + bookingId + ".");
        }
        if (approved) {
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new ValidationException("Статус бронирования id=" + bookingId + " уже подтвержён.");
            }
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        booking = repository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingDtoById(Long bookingId, Long bookerId) {
        userService.getUser(bookerId);
        Booking booking = getBookingById(bookingId);
        Item item = booking.getItem();
        if (!booking.getBooker().getId().equals(bookerId) && !Objects.equals(item.getOwner().getId(), bookerId)) {
            throw new NotFoundException("У пользователя id=" + bookerId + " нет прав для просмотраа брониирования id="
                    + bookingId + ".");
        }
        log.info("Получено бронирование id={}", bookingId);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование id=" + bookingId + "не найдено."));
    }

    @Override
    public List<BookingDto> getBookingsByBooker(Long bookerId, String state) {
        userService.getUser(bookerId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = repository.findByBookerIdOrderByStartDesc(bookerId);
                break;
            case "CURRENT":
                bookings = repository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookings = repository.findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = repository.findByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = repository.findByBookerIdAndStatusOrderByStartDesc(bookerId, Status.WAITING);
                break;
            case "REJECTED":
                bookings = repository.findByBookerIdAndStatusOrderByStartDesc(bookerId, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsForBookerItems(Long bookerId, String state) {
        userService.getUser(bookerId);
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = repository.findAllByItemOwnerIdOrderByStartDesc(bookerId);
                break;
            case "CURRENT":
                bookings = repository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookings = repository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = repository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = repository.findAllByItemOwnerIdAndStatusOrderByStartDesc(bookerId, Status.WAITING);
                break;
            case "REJECTED":
                bookings = repository.findAllByItemOwnerIdAndStatusOrderByStartDesc(bookerId, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void validateDate(BookingCreateDto booking) {
        if (booking.getStart().equals(booking.getEnd())
                || booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Дата начала бронирования должна быть раньше даты окончания бронирования.");
        }
    }
}

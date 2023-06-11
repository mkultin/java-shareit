package ru.practicum.shareit;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Entities {

    public static final User user = new User(1L, "User1", "User1@email.com");

    public static final UserDto userDto1 = UserDto.builder()
            .id(1L)
            .name("User1")
            .email("User1@email.com")
            .build();

    public static final UserDto userDto2 = UserDto.builder()
            .id(2L)
            .name("User2")
            .email("User2@email.com")
            .build();

    public static final UserDto userDto3 = UserDto.builder()
            .id(3L)
            .email("User3@email.com")
            .name("User3")
            .build();

    public static final ItemGetDto itemGetDto = ItemGetDto.builder()
            .id(1L)
            .name("Item1")
            .description("Description1")
            .available(true)
            .build();

    public static final CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .text("text")
            .authorName(userDto1.getName())
            .created(LocalDateTime.now())
            .build();

    public static final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("description")
            .available(true)
            .build();

    public static final ItemDto itemDto1 = ItemDto.builder()
            .id(1L)
            .name("Item1")
            .description("Description1")
            .available(true)
            .build();

    public static final ItemDto itemDto2 = ItemDto.builder()
            .name("Item2")
            .description("Description2")
            .available(true)
            .build();

    public static final ItemDto itemDto3 = ItemDto.builder()
            .name("Item3")
            .description("Description3")
            .available(true)
            .build();

    public static final ItemDto itemDto4 = ItemDto.builder()
            .name("Item4")
            .description("Description4")
            .available(true)
            .requestId(1L)
            .build();

    public static final BookingCreateDto bookingCreateDto1 = BookingCreateDto.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusNanos(2L))
            .itemId(2L)
            .build();

    public static final BookingCreateDto bookingCreateDto2 = BookingCreateDto.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusNanos(2L))
            .itemId(1L)
            .build();

    public static final ItemRequestDto itemRequestDto1 = ItemRequestDto.builder()
            .description("item4")
            .requestorId(2L)
            .build();

    public static final ItemRequestDto itemRequestDto2 = ItemRequestDto.builder()
            .description("item5")
            .requestorId(1L)
            .build();

    public static ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("Description")
            .created(LocalDateTime.now())
            .requestorId(1L)
            .build();

    public static List<BookingDto> bookings = new ArrayList<>();
}

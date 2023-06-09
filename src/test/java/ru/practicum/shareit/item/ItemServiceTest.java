package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.expections.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
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
public class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    BookingService bookingService;
    @Autowired
    ItemRequestService itemRequestService;

    UserDto user1 = UserDto.builder()
            .name("User1")
            .email("user1@email.com")
            .build();

    UserDto user2 = UserDto.builder()
            .name("User2")
            .email("user2@email.com")
            .build();

    CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .text("text")
            .authorName(user1.getName())
            .created(LocalDateTime.now())
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

    BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusNanos(2L))
            .itemId(2L)
            .build();

    @BeforeEach
    void beforeEach() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(itemDto1, 1L);
        itemService.create(itemDto2, 2L);
        itemService.create(itemDto3, 2L);
    }

    @Test
    void createTest() {
        ItemDto itemDto4 = ItemDto.builder()
                .name("Item4")
                .description("Description4")
                .available(true)
                .build();

        itemService.create(itemDto4, 2L);

        List<ItemGetDto> items = itemService.getItemsByOwner(2L, 0, 10);

        assertThat(items.size(), equalTo(3));
        assertThat(itemService.getItemById(4L, 2L).getDescription(),
                equalTo(itemDto4.getDescription()));
    }

    @Test
    void updateTest() {
        ItemDto itemToUpdate = ItemDto.builder()
                .name("ItemName")
                .available(false)
                .build();

        itemService.update(itemToUpdate, 2L, 3L);

        ItemGetDto updatedItem = itemService.getItemById(3L, 2L);

        assertThat(updatedItem.getDescription(), equalTo(itemDto3.getDescription()));
        assertThat(updatedItem.getName(), equalTo(itemToUpdate.getName()));
        assertThat(updatedItem.getAvailable(), equalTo(itemToUpdate.getAvailable()));

        assertThrows(NotFoundException.class, () -> itemService.update(itemToUpdate, 1L, 2L));
        assertThrows(NotFoundException.class, () -> itemService.update(itemToUpdate, 3L, 1L));
    }

    @Test
    void getItemById() {
        assertThrows(NotFoundException.class, () -> itemService.getItemById(17L, 1L));

        bookingService.create(bookingCreateDto, 1L);
        bookingService.approveBooking(1L, 2L, true);
        itemService.createComment(commentDto, 2L, 1L);

        assertThat(itemService.getItemById(2L, 2L).getLastBooking().getId(), equalTo(1L));
        assertThat(itemService.getItemById(2L, 1L).getComments().size(), equalTo(1));
    }

    @Test
    void search() {
        assertThat(itemService.search("", 0, 10).size(), equalTo(0));
        assertThat(itemService.search("iTEm", 0, 10).size(), equalTo(3));
        assertThat(itemService.search("SCRIPT", 0, 10).size(), equalTo(3));
        assertThat(itemService.search("2", 0, 10).size(), equalTo(1));
    }

    @Test
    void createComment() {
        bookingService.create(bookingCreateDto, 1L);
        bookingService.approveBooking(1L, 2L, true);
        CommentDto savedComment = itemService.createComment(commentDto, 2L, 1L);

        assertThrows(ValidationException.class, () -> itemService.createComment(commentDto, 1L, 2L));
        assertThat(savedComment.getText(), equalTo(commentDto.getText()));
    }

    @Test
    void getItemsByRequest() {
        ItemRequestDto itemRequestDto = itemRequestService.create(ItemRequestDto.builder()
                .description("item4")
                .requestorId(2L)
                .build(), 2L);
        ItemDto itemDto = itemService.create(ItemDto.builder()
                .name("Item4")
                .description("Description4")
                .requestId(itemRequestDto.getId())
                .available(true)
                .build(), 1L);

        assertThat(itemService.getItemsByRequest(itemRequestDto.getId()).size(), equalTo(1));
        assertThat(itemService.getItemsByRequest(itemRequestDto.getId()).get(0), equalTo(itemDto));
    }
}

package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.expections.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
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

    ItemRequestDto itemRequestDto1 = ItemRequestDto.builder()
            .description("item4")
            .requestorId(2L)
            .build();

    ItemRequestDto itemRequestDto2 = ItemRequestDto.builder()
            .description("item5")
            .requestorId(1L)
            .build();

    @BeforeEach
    void beforeEach() {
        userService.create(user1);
        userService.create(user2);
        itemService.create(itemDto1, 1L);
        itemService.create(itemDto2, 2L);
        itemService.create(itemDto3, 2L);
        itemRequestService.create(itemRequestDto1, 2L);
    }

    @Test
    void create() {
        itemRequestService.create(itemRequestDto2, 1L);

        assertThrows(NotFoundException.class, () -> itemRequestService.create(itemRequestDto2, 3L));
        assertThat(itemRequestService.getItemRequestById(1L, 2L).getDescription(),
                equalTo(itemRequestDto1.getDescription()));

        List<ItemRequestDto> itemRequests = itemRequestService.getAllItemRequests(1L, 0, 10);

        assertThat(itemRequests.size(), equalTo(1));
    }

    @Test
    void getItemRequestsByRequestor() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestsByRequestor(3L));
        assertThat(itemRequestService.getItemRequestsByRequestor(1L).size(), equalTo(0));
        assertThat(itemRequestService.getItemRequestsByRequestor(2L).size(), equalTo(1));

        ItemDto itemDto4 = ItemDto.builder()
                .name("Item4")
                .description("Description4")
                .available(true)
                .requestId(1L)
                .build();
        ItemDto itemDto = itemService.create(itemDto4, 1L);

        List<ItemRequestDto> items = itemRequestService.getItemRequestsByRequestor(2L);

        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getItems().size(), equalTo(1));
        assertThat(items.get(0).getItems().get(0), equalTo(itemDto));
    }

    @Test
    void getItemRequestById() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestById(2L, 1L));

        ItemDto itemDto4 = ItemDto.builder()
                .name("Item4")
                .description("Description4")
                .available(true)
                .requestId(1L)
                .build();
        itemService.create(itemDto4, 1L);

        ItemRequestDto itemRequestDto = itemRequestService.getItemRequestById(1L, 1L);

        assertThat(itemRequestDto.getId(), equalTo(1L));
        assertThat(itemRequestDto.getItems().size(), equalTo(1));
    }

    @Test
    void getAllItemRequests() {
        itemRequestService.create(itemRequestDto2, 1L);
        itemRequestService.create(ItemRequestDto.builder()
                .description("item6")
                .requestorId(1L)
                .build(), 1L);

        List<ItemRequestDto> itemRequests = itemRequestService.getAllItemRequests(2L, 0, 10);

        assertThat(itemRequests.size(), equalTo(2));

        assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequests(2L, -1, 131));
        assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequests(2L, 1, -131));
    }
}

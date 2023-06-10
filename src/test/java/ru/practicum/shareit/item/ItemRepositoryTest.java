package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.Entities.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    User user2 = new User(2L, "User2", "User2@email.com");

    private final ItemRequest itemRequest = ItemRequest.builder()
            .description("item4")
            .requestor(user2)
            .created(LocalDateTime.now())
            .build();

    @BeforeEach
    void beforeEach() {
        userRepository.save(user);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest);
        itemRepository.save(ItemMapper.toItem(itemDto1, user, itemRequest));
    }

    @Test
    void findByOwnerId() throws Exception {
        assertThat(itemRepository.findByOwnerId(user.getId(), PageRequest.ofSize(5)).size(),
                equalTo(1));

        assertThat(itemRepository.findByOwnerId(user2.getId(), PageRequest.ofSize(5)).size(),
                equalTo(0));
    }

    @Test
    void findBySearchQuery() throws Exception {
        assertThat(itemRepository.findBySearchQuery("iitem", PageRequest.ofSize(5)).size(),
                equalTo(0));
        assertThat(itemRepository.findBySearchQuery("item", PageRequest.ofSize(5)).size(),
                equalTo(1));
    }

    @Test
    void findByRequestId() throws Exception {
        assertThat(itemRepository.findByRequestId(itemRequest.getId()).size(),
                equalTo(1));
    }


}

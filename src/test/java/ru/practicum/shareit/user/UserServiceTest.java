package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.Entities.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {
    @Autowired
    private final UserService userService;

    @BeforeEach
    void beforeEach() {
        userService.create(userDto1);
        userService.create(userDto2);
        userService.create(userDto3);
    }

    @Test
    void create() {
        UserDto userDto4 = UserDto.builder()
                .id(4L)
                .email("user4@email.ru")
                .name("User4")
                .build();

        userService.create(userDto4);

        List<UserDto> users = userService.getUsers();

        assertThat(users.size(), equalTo(4));
        assertThat(users, equalTo(List.of(userDto1, userDto2, userDto3, userDto4)));
    }

    @Test
    void getUsers() {
        List<UserDto> users = userService.getUsers();

        assertThat(users.size(), equalTo(3));
        assertThat(users, equalTo(List.of(userDto1, userDto2, userDto3)));
    }

    @Test
    void getUserById() {
        UserDto user = userService.getUserById(1L);

        assertThat(user, equalTo(userDto1));
        assertThrows(NotFoundException.class, () -> userService.getUserById(10L));
    }

    @Test
    void updateUser() {
        UserDto user = UserDto.builder()
                .email("update@mail.ru")
                .name("update")
                .build();

        userService.updateUser(3L, user);

        UserDto updatedUser = userService.getUserById(3L);

        assertThat(updatedUser.getId(), equalTo(3L));
        assertThat(updatedUser.getName(), equalTo(user.getName()));
        assertThat(updatedUser.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void delete() {
        userService.delete(3L);

        assertThrows(NotFoundException.class, () -> userService.getUserById(3L));

        List<UserDto> users = userService.getUsers();

        assertThat(users.size(), equalTo(2));
        assertThat(users, equalTo(List.of(userDto1, userDto2)));
    }
}

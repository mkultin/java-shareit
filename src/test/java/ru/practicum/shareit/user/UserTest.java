package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Entities;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    private final User sameUser = new User(1L, "User1", "User1@email.com");

    @Test
    void testUserEqualsAndHashcode() {
        assertEquals(sameUser, Entities.user);
        assertTrue(sameUser.hashCode() == Entities.user.hashCode());
    }
}

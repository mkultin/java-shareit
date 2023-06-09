package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    User user = new User(1L, "user", "user@email.com");
    User user2 = new User(1L, "user", "user@email.com");

    @Test
    void testUserEqualsAndHashcode() {
        assertEquals(user, user2);
        assertTrue(user.hashCode() == user2.hashCode());
    }
}

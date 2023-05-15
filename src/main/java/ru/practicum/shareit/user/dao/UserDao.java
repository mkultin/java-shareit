package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getUsers();

    Optional<User> getUserById(Long id);

    User create(User user);

    void delete(Long id);
}

package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    List<User> getUsers();

    User getUserById(Long id);

    List<User> getUsersByIds(Set<Long> ids);

    User create(User user);

    User updateUser(User user);

    void delete(Long id);
}

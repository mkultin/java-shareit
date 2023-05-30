package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUserById(Long id);

    UserDto create(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    void delete(Long id);

    User getUser(Long id);
}

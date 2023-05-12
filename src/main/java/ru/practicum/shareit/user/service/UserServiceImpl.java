package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserDto> getUsers() {
        return userDao.getUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userDao.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден."));
        log.info("Получен пользователь name={}, id={}", user.getName(), user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        return UserMapper.toUserDto(userDao.create(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        userDto.setId(id);
        User userToUpdate = userDao.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден."));
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) userDto.setEmail(userToUpdate.getEmail());
        if (userDto.getName() == null || userDto.getName().isBlank()) userDto.setName(userToUpdate.getName());
        return UserMapper.toUserDto(userDao.updateUser(UserMapper.toUser(userDto)));
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }
}

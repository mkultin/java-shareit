package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

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
        return UserMapper.toUserDto(userDao.getUserById(id));
    }

    @Override
    public UserDto create(User user) {
        return UserMapper.toUserDto(userDao.create(user));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        userDto.setId(id);
        User userToUpdate = userDao.getUserById(id);
        if (userDto.getEmail() == null) {
            userDto.setEmail(userToUpdate.getEmail());
        }
        if (userDto.getName() == null) {
            userDto.setName(userToUpdate.getName());
        }
        return UserMapper.toUserDto(userDao.updateUser(UserMapper.toUser(userDto)));
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }
}

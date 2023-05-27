package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserDbService implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        log.info("Получены все пользователи");
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден."));
        log.info("Получен пользователь name={}, id={}", user.getName(), user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info("Добавлен пользователь id={}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        userDto.setId(id);
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден."));
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) userToUpdate.setEmail(userDto.getEmail());
        if (userDto.getName() != null && !userDto.getName().isBlank()) userToUpdate.setName(userDto.getName());
        User updatedUser = userRepository.save(userToUpdate);
        log.info("Обновлён пользователь id={}", updatedUser.getId());
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("Удалён пользователь id={}", id);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден."));
    }
}

package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.expections.ConflictException;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserDao implements UserDao {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
    }

    @Override
    public List<User> getUsersByIds(Set<Long> ids) {
        return null;
    }

    @Override
    public User create(User user) {
        validateEmail(user.getEmail());
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Создан новый пользователь {}, id={}", user.getName(), user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден.");
        }
        User userToUpdate = users.get(user.getId());
        if (!user.getEmail().equals(userToUpdate.getEmail())) {
            validateEmail(user.getEmail());
        }
        users.put(user.getId(), user);
        log.info("Обновлен пользователь {}, id={}", user.getName(), user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        users.remove(id);
        log.info("Пользователь id={} удален", id);
    }

    private void validateEmail(String email) {
        List<String> emails = users.values().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (emails.contains(email)) {
            throw new ConflictException("Пользователь с Email= " + email + " уже существует");
        }
    }
}

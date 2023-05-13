package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.*;

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
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Создан новый пользователь {}, id={}", user.getName(), user.getId());
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
}

package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryItemDao implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    @Override
    public Item create(Item item) {
        item.setId(id++);
        items.put(item.getId(), item);
        log.info("Пользователем id={} добавлена новая вещь name={}, id={}",
                item.getOwner(), item.getName(), item.getId());
        return item;
    }

    @Override
    public Item update(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException("Вещь с id = " + item.getId() + "не найдена.");
        }
        items.put(item.getId(), item);
        log.info("Обновлена вещь name={}, id={}", item.getName(), item.getId());
        return item;
    }

    @Override
    public Item getItemById(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещь с id = " + itemId + "не найдена.");
        }
        Item item = items.get(itemId);
        log.info("Получена вещь name={}, id={}", item.getName(), item.getId());
        return item;
    }

    @Override
    public List<Item> getItemsByOwner(Long userId) {
        List<Item> itemsByOwner = items.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .collect(Collectors.toList());
        log.info("Получены вещи для пользователя id={}.", userId);
        return itemsByOwner;
    }

    @Override
    public List<Item> search(String text) {
        List<Item> itemsByOwner = items.values().stream()
                .filter(item -> Objects.equals(item.getAvailable(), true))
                .filter(item -> item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
        log.info("Получены вещи по запросу text={}.", text);
        return itemsByOwner;
    }
}

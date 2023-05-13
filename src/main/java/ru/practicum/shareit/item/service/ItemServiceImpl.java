package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao, UserService userService) {
        this.itemDao = itemDao;
        this.userService = userService;
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        userService.getUserById(ownerId);
        return ItemMapper.toItemDto(itemDao.create(ItemMapper.toItem(itemDto, ownerId)));
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) {
        if (itemDto.getId() == null) itemDto.setId(itemId);
        userService.getUserById(ownerId);
        Item itemToUpdate = itemDao.getItemById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + "не найдена."));
        if (!Objects.equals(itemToUpdate.getOwner(), ownerId)) {
            throw new NotFoundException("Вещь id=" + itemId + " у пользователя id=" +
                    itemToUpdate.getOwner() + "не найдена.");
        }
        Item item = ItemMapper.toItem(itemDto, ownerId);
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) itemToUpdate.setName(itemDto.getName());
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            itemToUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) itemToUpdate.setAvailable(itemDto.getAvailable());
        return ItemMapper.toItemDto(itemToUpdate);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemDao.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + "не найдена."));
        log.info("Получена вещь name={}, id={}", item.getName(), item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long userId) {
        return itemDao.getItemsByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        text = text.toLowerCase();
        if (text.isBlank()) return Collections.emptyList();
        return itemDao.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}

package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.expections.ValidationException;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemDbService implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        Item item = itemRepository.save(ItemMapper.toItem(
                itemDto, userService.getUser(ownerId)));
        log.info("Пользователем id={} добавлена новая вещь name={}, id={}",
                item.getOwner(), item.getName(), item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) {
        if (itemDto.getId() == null) itemDto.setId(itemId);
        userService.getUserById(ownerId);
        Item itemToUpdate = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + "не найдена."));
        if (!Objects.equals(itemToUpdate.getOwner().getId(), ownerId)) {
            throw new NotFoundException("Вещь id=" + itemId + " у пользователя id=" +
                    itemToUpdate.getOwner().getId() + " не найдена.");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) itemToUpdate.setName(itemDto.getName());
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            itemToUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) itemToUpdate.setAvailable(itemDto.getAvailable());
        Item item = itemRepository.save(itemToUpdate);
        log.info("Пользователем id={} обноввлена вещь name={}, id={}",
                item.getOwner().getId(), item.getName(), item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemGetDto getItemById(Long itemId, Long ownerId) {
        Item item = getItem(itemId);
        log.info("Получена вещь name={}, id={}", item.getName(), item.getId());
        if (item.getOwner().getId().equals(ownerId)) {
            return getItemDtoWithBookings(item);
        }
        return ItemMapper.toItemGetDto(item, getItemComments(item));
    }

    @Override
    public List<ItemGetDto> getItemsByOwner(Long userId) {
        List<Item> itemsByOwner = itemRepository.findByOwnerId(userId);
        log.info("Получен список вещей для пользователя id={}", userId);
        return itemsByOwner.stream()
                .map(this::getItemDtoWithBookings)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) return Collections.emptyList();
        List<Item> itemsBySearchQuery = itemRepository.findBySearchQuery(text.toLowerCase());
        log.info("Получены доступные вещи по запросу '{}'", text);
        return itemsBySearchQuery.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + "не найдена."));
    }

    @Transactional
    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long authorId) {
        Item item = getItem(itemId);
        User author = userService.getUser(authorId);
        BookingDtoForItem booking = bookingRepository.findFirstByItemIdAndBookerIdAndEndBeforeAndStatus(itemId,
                authorId, LocalDateTime.now(), Status.APPROVED);
        if (booking == null) {
            throw new ValidationException("Чтобы оставить комментарий для вещи " +
                    "id=" + itemId + "необходиимо иметь завершшенной успешное бронирование.");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, item, author));
        log.info("Добавлен комментарий для вещи id={} от пользователя id={}", itemId, authorId);
        return CommentMapper.toCommentDto(comment);
    }


    private ItemGetDto getItemDtoWithBookings(Item item) {
        BookingDtoForItem lastBooking = bookingRepository
                .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(),
                        Status.APPROVED);
        BookingDtoForItem nextBooking = bookingRepository
                .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(),
                        Status.APPROVED);
        return ItemMapper.toOwnersItemGetDto(item,
                lastBooking, nextBooking, getItemComments(item));
    }

    private List<CommentDto> getItemComments(Item item) {
        return commentRepository.findAllByItemIdOrderByCreatedDesc(item.getId());
    }
}

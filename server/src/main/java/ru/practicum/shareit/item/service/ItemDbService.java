package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.expections.ValidationException;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
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
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        Item itemToSave;
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с id = " + itemDto.getRequestId() + "не найден."));
            itemToSave = ItemMapper.toItem(itemDto, userService.getUser(ownerId), itemRequest);
        } else {
            itemToSave = ItemMapper.toItem(itemDto, userService.getUser(ownerId));
        }
        Item item = itemRepository.save(itemToSave);
        if (item.getRequest() != null) {
            log.info("Пользователем id={} по запросу id={} добавлена новая вещь name={}, id={}",
                    item.getOwner().getId(), item.getRequest().getId(), item.getName(), item.getId());
        } else {
            log.info("Пользователем id={} добавлена новая вещь name={}, id={}",
                    item.getOwner().getId(), item.getName(), item.getId());
        }
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
    public List<ItemGetDto> getItemsByOwner(Long userId, Integer from, Integer size) {
        List<Item> itemsByOwner = itemRepository.findByOwnerIdOrderById(userId, getPageByParams(from, size));
        log.info("Получен список вещей для пользователя id={}", userId);
        return itemsByOwner.stream()
                .map(this::getItemDtoWithBookings)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, Integer from, Integer size) {
        if (text.isBlank()) return Collections.emptyList();
        List<Item> itemsBySearchQuery = itemRepository.findBySearchQuery(text.toLowerCase(),
                getPageByParams(from, size));
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
        BookingShort booking = bookingRepository.findFirstByItemIdAndBookerIdAndEndBeforeAndStatus(itemId,
                authorId, LocalDateTime.now(), Status.APPROVED);
        if (booking == null) {
            throw new ValidationException("Чтобы оставить комментарий для вещи " +
                    "id=" + itemId + "необходиимо иметь завершшенной успешное бронирование.");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, item, author));
        log.info("Добавлен комментарий для вещи id={} от пользователя id={}", itemId, authorId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<ItemDto> getItemsByRequest(Long requestId) {
        List<Item> items = itemRepository.findByRequestId(requestId);
        log.info("Получены вещи, созданные по запросу id={}", requestId);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    private ItemGetDto getItemDtoWithBookings(Item item) {
        BookingShort lastBooking = bookingRepository
                .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(), LocalDateTime.now(),
                        Status.APPROVED);
        BookingShort nextBooking = bookingRepository
                .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(),
                        Status.APPROVED);
        return ItemMapper.toOwnersItemGetDto(item,
                lastBooking, nextBooking, getItemComments(item));
    }

    private List<CommentDto> getItemComments(Item item) {
        List<Comment> comments = commentRepository.findAllByItemIdOrderByCreatedDesc(item.getId());
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private PageRequest getPageByParams(Integer from, Integer size) {
        if (from < 0 || size < 0) throw new ValidationException("Переданы неверные параметры пагинации");
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}

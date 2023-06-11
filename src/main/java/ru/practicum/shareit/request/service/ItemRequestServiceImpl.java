package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.expections.NotFoundException;
import ru.practicum.shareit.expections.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long requestorId) {
        User requestor = userService.getUser(requestorId);
        ItemRequest itemRequest = itemRequestRepository.save(
                ItemRequestMapper.toItemRequest(itemRequestDto, requestor));
        log.info("Создан запрос id={}", itemRequest.getId());
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequestor(Long requestorId) {
        userService.getUser(requestorId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(requestorId);
        log.info("Получены запросы, созданные пользователем id={}", requestorId);
        return itemRequests.stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestDto(itemRequest,
                        itemService.getItemsByRequest(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequest getItemRequest(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос с id = " + requestId + "не найден."));
        log.info("Получен запрос id={}", requestId);
        return itemRequest;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long requestId, Long userId) {
        userService.getUser(userId);
        return ItemRequestMapper.toItemRequestDto(getItemRequest(requestId), itemService.getItemsByRequest(requestId));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        if (from < 0 || size < 0) throw new ValidationException("Переданы неверные параметры пагинации");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdIsNotOrderByCreatedDesc(userId, page);
        log.info("Получен список всех запросов от пользователей, кроме id={}", userId);
        return itemRequests.stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestDto(itemRequest,
                        itemService.getItemsByRequest(itemRequest.getId())))
                .collect(Collectors.toList());
    }
}

package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerIdOrderById(Long ownerId, PageRequest pageRequest);

    @Query("select i from Item i " +
            "where i.available = true " +
            "and (lower(i.name) like concat('%', ?1, '%') " +
            "or lower(i.description) like concat('%', ?1, '%'))")
    List<Item> findBySearchQuery(String query, PageRequest pageRequest);

    List<Item> findByRequestId(Long requestId);
}

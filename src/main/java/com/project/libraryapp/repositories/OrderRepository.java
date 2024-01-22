package com.project.libraryapp.repositories;

import com.project.libraryapp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser_IdAndBooks_Id(Long userId, Long bookId);

    List<Order> findByUserId(Long userId);

    List<Order> findByBooks_Id(Long bookId);
}

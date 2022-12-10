package com.francesco.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}

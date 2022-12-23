package com.gk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gk.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}

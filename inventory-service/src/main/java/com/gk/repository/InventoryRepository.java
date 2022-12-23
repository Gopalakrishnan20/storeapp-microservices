package com.gk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gk.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {



List<Inventory> findBySkuCodeIn(List<String> skuCode);

}
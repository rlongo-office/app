package com.webrpg.app.dao;

import com.webrpg.app.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    public List<Inventory> findAll();
    public Page<Inventory> findAll(Pageable pageable);
    public List<Inventory> findByName(String name);
    public List<Inventory> findById(long id);
    public List<Inventory> findAllById(long id);
    public List<Inventory> deleteById(long id);
    public boolean existsByName(String name);
    public long count();
}
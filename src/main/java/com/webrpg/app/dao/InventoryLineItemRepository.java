package com.webrpg.app.dao;

import com.webrpg.app.model.InventoryLineItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/*
Planning to also create a view that pulls in the Thing attributes for a full
Inventory List of Materials.
 */
public interface InventoryLineItemRepository extends JpaRepository<InventoryLineItem,Long> {
    public List<InventoryLineItem> findAll();
    public Page<InventoryLineItem> findAll(Pageable pageable);
    public List<InventoryLineItem> findById(long id);
    public List<InventoryLineItem> findAllById(long id);
    public List<InventoryLineItem> deleteById(long id);
    public long count();
}

package com.webrpg.app.model;

import javax.persistence.*;

@Entity
@Table(name = "inventory_line_item", schema = "gameworld", catalog = "game_test")
public class InventoryLineItem {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "quantity", nullable = true)
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "id_inventory", referencedColumnName = "id")
    private Inventory inventoryByIdInventory;
    @ManyToOne
    @JoinColumn(name = "id_inventory_line_item", referencedColumnName = "id")
    private InventoryLineItem inventoryLineItemByIdInventoryLineItem;
    @ManyToOne
    @JoinColumn(name = "id_thing", referencedColumnName = "id")
    //This relationship is necessary for  specifying what is the actual 'thing' that inventory line references
    private Thing inventoryLineItemByIdThing;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoryLineItem that = (InventoryLineItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    public Inventory getInventoryByIdInventory() {
        return inventoryByIdInventory;
    }
    public void setInventoryByIdInventory(Inventory inventoryByIdInventory) { this.inventoryByIdInventory = inventoryByIdInventory; }

    public InventoryLineItem getInventoryLineItemByIdInventoryLineItem() { return inventoryLineItemByIdInventoryLineItem; }
    public void setInventoryLineItemByIdInventoryLineItem(InventoryLineItem inventoryLineItemByIdInventoryLineItem) { this.inventoryLineItemByIdInventoryLineItem = inventoryLineItemByIdInventoryLineItem; }

    public Thing getInventoryLineItemByIdThing() { return inventoryLineItemByIdThing; }
    public void setInventoryLineItemByIdThing(Thing inventoryLineItemByIdThing) { this.inventoryLineItemByIdThing = inventoryLineItemByIdThing; }
}

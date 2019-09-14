package net.neferett.sellsigns.gui;

import lombok.Data;
import net.neferett.sellsigns.SellSigns;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Data
public abstract class GuiScreen implements Listener {

    Inventory inv;

    private final String name;
    private final Player player;
    private final int size;

    public void addItem(final ItemStack item) {
        this.inv.addItem(item);
    }

    public void build() {
        this.inv = SellSigns.getInstance().getServer().createInventory(this.player, this.size * 9, this.name);
    }

    public void clearInventory() {
        this.inv.clear();
    }

    public void close() {
        this.player.closeInventory();
    }

    abstract public void drawScreen();


    public abstract void onClick(ItemStack item, InventoryClickEvent event);

    public abstract void onClose();

    public abstract void onOpen();

    @EventHandler
    public void onPlayerInventory(final InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (!e.getClickedInventory().equals(this.inv))
            return;
        if (!this.isValid(e.getCurrentItem()))
            return;
        e.setCancelled(true);
        this.onClick(e.getCurrentItem(), e);
    }

    @EventHandler
    public void onPlayerInventory(final InventoryCloseEvent e) {
        this.onClose();
        if (!GuiManager.isOpened(this.getClass()))
            HandlerList.unregisterAll(this);
    }

    public void open() {
        this.player.openInventory(this.inv);
        this.drawScreen();
        this.player.updateInventory();
        SellSigns.getInstance().getServer().getPluginManager().registerEvents(this, SellSigns.getInstance());
        this.onOpen();
    }

    public void setFont(final ItemStack item) {
        for (int i = 0; i < this.inv.getSize(); i++)
            this.setItem(item, i);
    }

    public void setItem(final ItemStack item, final int slot) {
        this.inv.setItem(slot, item);
    }

    public void setItemLine(final ItemStack item, final int line, final int slot) {
        this.setItem(item, line * 9 - 9 + slot - 1);
    }

    private boolean isValid(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }

}

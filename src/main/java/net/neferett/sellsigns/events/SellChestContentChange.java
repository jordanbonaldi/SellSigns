package net.neferett.sellsigns.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Arrays;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class SellChestContentChange extends Event implements Cancellable {

    private final Player player;
    private final Chest chest;
    private final Sign sign;
    private boolean cancelled = false;

    private static HandlerList handlers = new HandlerList();

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public int getChestContent() {
        return (int) Arrays.stream(this.chest.getInventory().getContents()).filter(Objects::nonNull).count();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

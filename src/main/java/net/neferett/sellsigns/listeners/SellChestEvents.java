package net.neferett.sellsigns.listeners;

import net.neferett.sellsigns.events.SellChestContentChange;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SellChestEvents implements Listener {

    @EventHandler
    public void sellChestChange(SellChestContentChange event) {
        Sign sign = event.getSign();

        sign.setLine(3, "Items: " + event.getChestContent());
        sign.update();
    }

}

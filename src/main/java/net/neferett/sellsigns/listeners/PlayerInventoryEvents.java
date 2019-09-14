package net.neferett.sellsigns.listeners;

import net.neferett.sellsigns.config.ConfigReader;
import net.neferett.sellsigns.handlers.PlayerLocal;
import net.neferett.sellsigns.handlers.PlayersHandler;
import net.neferett.sellsigns.utils.SignUtils;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Arrays;
import java.util.Objects;

public class PlayerInventoryEvents implements Listener {

    ConfigReader configReader = ConfigReader.getInstance();

    @EventHandler
    public void playerCloseSellSignInventory(InventoryCloseEvent event) {
        PlayerLocal local = PlayersHandler.getInstance().getPlayerLocal((Player) event.getPlayer());

        if (local.getData().get("opened") != null) {
            Chest chest = (Chest) ((Location)local.getData().get("opened")).getBlock().getState();

            Arrays.stream(chest.getInventory().getContents()).filter(Objects::nonNull).forEach(e -> SignUtils.dropItems(chest, (Player)event.getPlayer(), e));

            local.getData().put("opened", null);
        }

    }

}

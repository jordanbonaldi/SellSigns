package net.neferett.sellsigns.listeners;

import lombok.SneakyThrows;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.api.exception.PlayerDataNotLoadedException;
import net.brcdev.shopgui.shop.ShopItem;
import net.neferett.sellsigns.SellSigns;
import net.neferett.sellsigns.config.ConfigReader;
import net.neferett.sellsigns.utils.SignUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.Objects;

public class PlayerPlaceSignEvent implements Listener {

    ConfigReader configReader = ConfigReader.getInstance();

    @EventHandler
    public void placeSignOnChest(SignChangeEvent event) {
        Sign sign = (Sign)event.getBlock().getState().getData();

        if (SignUtils.checkSignByEvent(event)) return;

        Block attached = event.getBlock().getRelative(sign.getAttachedFace());
        Chest chest = attached.getType() == Material.CHEST ? (Chest) attached.getState() : null;

        if (chest == null)
            return;

        Arrays.stream(chest.getInventory().getContents()).filter(Objects::nonNull).forEach(e -> SignUtils.dropItems(chest, event.getPlayer(), e));

        event.setLine(2, event.getPlayer().getName());
        event.setLine(3, "Items: " + Arrays.stream(chest.getInventory().getContents()).filter(Objects::nonNull).count());
    }

}

package net.neferett.sellsigns.utils;

import lombok.SneakyThrows;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.neferett.sellsigns.SellSigns;
import net.neferett.sellsigns.config.ConfigReader;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SignUtils {

    public static boolean checkSignByEvent(BlockEvent event) {
        return checkSignLines((Sign)event);
    }

    public static boolean checkSignLines(Sign sign) {
        ConfigReader configReader = SellSigns.getInstance().getConfigReader();

        return Arrays.stream(sign.getLines())
                .filter(e -> configReader.getDenominations().contains(e.replaceAll("\\[(.*?)]", "$1").toLowerCase()))
                .findAny().orElse(null) == null;
    }

    @SneakyThrows
    public static void dropItems(Chest chest, Player p, ItemStack itemStack) {
        if (ShopGuiPlusApi.getItemStackPriceSell(p, itemStack) >= 0)
            return;

        chest.getInventory().remove(itemStack);
        p.getLocation().getWorld().dropItemNaturally(p.getLocation().add(0, 1, 1), itemStack);
        p.sendMessage(ConfigReader.getInstance().accessMessage("cantsell") + " : " +
                (itemStack.hasItemMeta() ? itemStack.getItemMeta().getDisplayName() : itemStack.getType().toString()) + " x" + itemStack.getAmount());
    }
}

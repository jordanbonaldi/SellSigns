package net.neferett.sellsigns.listeners;

import lombok.SneakyThrows;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.shop.ShopItem;
import net.neferett.sellsigns.SellSigns;
import net.neferett.sellsigns.config.ConfigReader;
import net.neferett.sellsigns.events.SellChestContentChange;
import net.neferett.sellsigns.gui.GuiManager;
import net.neferett.sellsigns.gui.guis.SellGuiScreen;
import net.neferett.sellsigns.handlers.PlayersHandler;
import net.neferett.sellsigns.utils.BlockUtils;
import net.neferett.sellsigns.utils.SignUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.Objects;

public class PlayerInteractEvents implements Listener {

    ConfigReader configReader = ConfigReader.getInstance();

    /**
     * Checking if sellsign exists when clicking on any sign
     *
     * @param event
     */
    @EventHandler
    @SneakyThrows
    public void onPlayerClickOnSign(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                event.getClickedBlock() == null ||
                !event.getClickedBlock().getType().equals(Material.WALL_SIGN) ||
                player.getItemInHand().getType() == null || player.getItemInHand().getType().equals(Material.AIR)
        )
            return;

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (SignUtils.checkSignLines(sign)) return;

        ShopItem shopItem = ShopGuiPlusApi.getItemStackShopItem(player, player.getItemInHand());

        if (shopItem == null) {
            player.sendMessage(configReader.accessMessage("cantsell"));

            return;
        }

//        double sellPrice = configReader.isShopguiplus() ? shopItem.getSellPrice() : shopItem.getSellPriceForAmount(player.getItemInHand().getAmount());
//
//        if (configReader.isShopguiplus()) {
//            if (player.getItemInHand().getAmount() >= shopItem.getItem().getAmount())
//                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - shopItem.getItem().getAmount());
//            else player.sendMessage(configReader.accessMessage("notenoughitems"));
//        } else
//            player.getItemInHand().setAmount(0);
//
//        System.out.println(sellPrice);
    }

    /**
     * Checking if front sign il a sellsign
     *
     * @param event
     */
    @EventHandler
    public void onPlayerClickOnChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                event.getClickedBlock() == null ||
                !event.getClickedBlock().getType().equals(Material.CHEST)
        )
            return;

        Chest chest = (Chest) event.getClickedBlock().getState();

        if (chest == null)
            return;

        Sign locatedSign = BlockUtils.getBlockInRange(chest.getLocation(), 1, 1)
                .stream().filter(e -> e.getType().equals(Material.WALL_SIGN)).map(e -> (Sign)e.getState())
                .filter(e -> !SignUtils.checkSignLines(e)).findFirst().orElse(null);

        if (locatedSign == null)
            return;

        Player locatedPlayer = Bukkit.getPlayer(locatedSign.getLine(2));

        if (player != locatedPlayer) {
            GuiManager.openGui(new SellGuiScreen(player, chest));
            event.setCancelled(true);
            return;
        }

        PlayersHandler.getInstance().setData(player, "opened", chest.getLocation());

        this.createCheckingTask(player, chest, locatedSign);
    }

    private void createCheckingTask(Player player, Chest chest, Sign sign) {
        SellSigns.getInstance().getTasksExecutor().addTask(() -> {
            PlayersHandler handler = PlayersHandler.getInstance();

            while (handler.getData(player, "opened") != null);

            Bukkit.getPluginManager().callEvent(new SellChestContentChange(player, chest, sign));
        });
    }

}

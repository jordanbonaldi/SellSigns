package net.neferett.sellsigns.gui.guis;

import lombok.SneakyThrows;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.shop.ShopItem;
import net.neferett.sellsigns.SellSigns;
import net.neferett.sellsigns.config.ConfigReader;
import net.neferett.sellsigns.gui.GuiScreen;
import net.neferett.sellsigns.utils.ItemBuilder;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class SellGuiScreen extends GuiScreen {

    private final Chest chest;
    private ConfigReader configReader;

    public SellGuiScreen(Player p, Chest chest) {
        super("Sell Inventory", p, 4);

        this.chest = chest;
        this.configReader = ConfigReader.getInstance();
        this.build();
    }

    @SneakyThrows
    private void fillItems(ItemStack item) {
        ShopItem shopItem = ShopGuiPlusApi.getItemStackShopItem(this.getPlayer(), item);

        double balance = SellSigns.getInstance().getEconomy().getBalance(this.getPlayer());
        addItem(
                new ItemBuilder(item).addLores(
                        configReader.accessMessage("price") + " " + shopItem.getSellPriceForAmount(item.getAmount()),
                        "",
                        configReader.accessMessage("amount") + " " + item.getAmount(),
                        "",
                        configReader.accessMessage(balance < shopItem.getSellPriceForAmount(item.getAmount()) ? "notenoughmoney" : "buy")
                ).build()
        );
    }

    @Override
    @SneakyThrows
    public void drawScreen() {
        Arrays.stream(this.chest.getInventory().getContents()).filter(Objects::nonNull).forEach(this::fillItems);
    }

    @Override
    @SneakyThrows
    public void onClick(ItemStack item, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT)
            return;

        Player player = (Player) event.getWhoClicked();

        ShopItem shopItem = ShopGuiPlusApi.getItemStackShopItem(player, item);
        double sellPrice = shopItem.getSellPriceForAmount(item.getAmount());

        double balance = SellSigns.getInstance().getEconomy().getBalance(player);

        if (balance < sellPrice) {
            player.sendMessage(configReader.accessMessage("notenoughmoney"));

            player.closeInventory();
            return;
        }

        SellSigns.getInstance().getEconomy().withdrawPlayer(player, sellPrice);

        player.sendMessage(configReader.accessMessage("paiditem") + " $" + sellPrice);

        ItemStack itemStack = Arrays.stream(this.chest.getInventory().getContents()).filter(Objects::nonNull).filter(e ->
                e.getType() == item.getType() && e.getAmount() == item.getAmount()
        ).findFirst().orElse(null);

        if (itemStack == null)
            return;

        this.chest.getInventory().remove(itemStack);

        if (player.getInventory().firstEmpty() < 0) {
            player.getLocation().getWorld().dropItemNaturally(player.getLocation().add(0, 1, 0), item);
            player.sendMessage(configReader.accessMessage("inventoryfull"));
        } else
            player.getInventory().addItem(item);

        player.closeInventory();
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onOpen() {

    }
}

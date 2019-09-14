package net.neferett.sellsigns;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.neferett.sellsigns.config.ConfigReader;
import net.neferett.sellsigns.handlers.PlayersHandler;
import net.neferett.sellsigns.listeners.PlayerInteractEvents;
import net.neferett.sellsigns.listeners.PlayerInventoryEvents;
import net.neferett.sellsigns.listeners.PlayerPlaceSignEvent;
import net.neferett.sellsigns.listeners.SellChestEvents;
import net.neferett.sellsigns.tasks.TasksExecutor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SellSigns extends JavaPlugin {

    @Getter
    private static SellSigns instance;

    private Economy economy;
    private ConfigReader configReader;
    private PlayersHandler playersHandler;
    private TasksExecutor tasksExecutor;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        return (this.economy = rsp.getProvider()) != null;
    }

    private void setupShopGuiPlus() {
        if (this.getServer().getPluginManager().getPlugin("ShopGUIPlus") == null)
            this.getServer().getPluginManager().disablePlugin(this);
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new PlayerInteractEvents(), this);
        this.getServer().getPluginManager().registerEvents(new SellChestEvents(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInventoryEvents(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerPlaceSignEvent(), this);
    }

    @Override
    public void onEnable() {
        if (!this.setupEconomy() ) {
            this.getServer().getPluginManager().disablePlugin(this);

            return;
        }

        this.setupShopGuiPlus();

        instance = this;
        this.configReader = new ConfigReader();
        this.playersHandler = new PlayersHandler();
        this.tasksExecutor = new TasksExecutor();

        new Thread(this.tasksExecutor, "ExecutorThread").start();

        this.registerEvents();
    }

}

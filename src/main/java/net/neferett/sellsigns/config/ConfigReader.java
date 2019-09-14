package net.neferett.sellsigns.config;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import net.neferett.sellsigns.SellSigns;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

@Getter
public class ConfigReader implements Reader{

    private final File file;

    private final FileConfiguration configfile;

    public static ConfigReader getInstance() {
        return SellSigns.getInstance().getConfigReader();
    }

    private final List<String> denominations;
    private final boolean shopguiplus;
    private final boolean dropNotSellableItems;

    public ConfigReader() {
        this.file = new File(SellSigns.getInstance().getDataFolder() + "/config.yml");

        if (!this.file.exists())
            SellSigns.getInstance().saveResource("config.yml", false);

        this.configfile = YamlConfiguration.loadConfiguration(this.file);

        this.save();

        /* Loading Config Values */

        {
            this.denominations = this.configfile.getStringList("shop.denominations");
            this.shopguiplus = this.configfile.getBoolean("shop.use_shopguiplus_itemamount");
            this.dropNotSellableItems = this.configfile.getBoolean("shop.drop_out_not_sellable_items");
        }
    }

    @SneakyThrows
    @Override
    public void save() {
        this.configfile.save(this.file);
    }

    public String accessMessage(String key) {
        return this.configfile.getString("messages."+ key).replaceAll("&", "§");
    }
}

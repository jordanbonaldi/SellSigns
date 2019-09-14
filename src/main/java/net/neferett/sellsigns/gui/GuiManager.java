package net.neferett.sellsigns.gui;

import net.neferett.sellsigns.SellSigns;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GuiManager {

    static HashMap<String,Class> openGuis = new HashMap<>();

    public static GuiScreen openGui(GuiScreen gui) {

        openPlayer(gui.getPlayer(), gui.getClass());

        gui.open();

        return gui;
    }

    @SuppressWarnings("rawtypes")
    public static void openPlayer(Player p, Class gui) {
        if (openGuis.containsKey(p.getName())) {
            openGuis.remove(p.getName());
            openGuis.put(p.getName(), gui);
        } else {
            openGuis.put(p.getName(), gui);
        }
    }

    public static void closePlayer(Player p) {
        openGuis.remove(p.getName());
    }

    public static boolean isPlayer(Player p) {
        if (openGuis.containsKey(p.getName())) return true;
        return false;
    }

    public static boolean isOpened(Class clazz) {
        for (Class cla : openGuis.values())
            if (cla.equals(clazz)) return true;

        return false;
    }
}

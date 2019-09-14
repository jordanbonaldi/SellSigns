package net.neferett.sellsigns.handlers;

import lombok.Data;
import org.bukkit.entity.Player;

import java.util.HashMap;

@Data
public class PlayerLocal {

    private final Player player;

    private HashMap<String, Object> data = new HashMap<>();

}

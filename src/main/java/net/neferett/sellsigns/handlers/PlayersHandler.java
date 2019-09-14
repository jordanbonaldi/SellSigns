package net.neferett.sellsigns.handlers;

import lombok.Data;
import net.neferett.sellsigns.SellSigns;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Data
public class PlayersHandler {

    public static PlayersHandler getInstance() {
        return SellSigns.getInstance().getPlayersHandler();
    }

    private HashMap<UUID, PlayerLocal> players = new HashMap<>();

    public PlayerLocal getPlayerLocal(Player player) {
        if (!this.players.containsKey(player.getUniqueId())) {
            PlayerLocal playerLocal = new PlayerLocal(player);

            this.players.put(player.getUniqueId(), playerLocal);
            return playerLocal;
        }

        return this.getPlayers().get(player.getUniqueId());
    }

    public void setData(Player player, String key, Object value) {
        this.getPlayerLocal(player).getData().put(key, value);
    }

    public Object getData(Player player, String key) {
        return this.getPlayerLocal(player).getData().get(key);
    }
}

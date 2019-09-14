package net.neferett.sellsigns.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {

    /**
     * Gets a list of near block
     * @param center
     * @param range
     * @param yRange
     * @return
     */
    public static List<Block> getBlockInRange(Location center, int range, int yRange) {
        List<Block> list = new ArrayList<Block>();
        Block b = center.getBlock();
        for(Vector v: VectorUtils.getVectorRange(range, yRange)) {
            list.add(b.getRelative(v.getBlockX(),v.getBlockY(),v.getBlockZ()));
        }
        return list;
    }

}

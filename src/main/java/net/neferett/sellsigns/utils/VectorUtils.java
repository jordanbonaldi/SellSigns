package net.neferett.sellsigns.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.util.Vector;


public class VectorUtils {
    private static HashMap<String, ArrayList<Vector>> vectorRanges;

    /**
     * @param range
     * @param yRange
     * @return
     */
    @SuppressWarnings("unchecked")
    static ArrayList<Vector> getVectorRange(int range, int yRange) {
        if (range < 0) range = -range;
        if (yRange < 0) yRange = -yRange;
        String key = range + ";" + yRange;
        if (vectorRanges == null) vectorRanges = new HashMap<>();
        if (!vectorRanges.containsKey(key)) {
            ArrayList<Vector> list = new ArrayList<Vector>();
            Vector center = new Vector(0,0,0);
            Vector v;
            int rangeSquared =  (1 + range) * (1 + range); //Begin at 1
            for (int x = - range; x <= range; x++) {
                for (int z = - range; z <= range; z++) {
                    for (int y = - yRange; y <= yRange; y++) {
                        v = new Vector(x,y,z);
                        if (v.distanceSquared(center) < rangeSquared)list.add(v);
                    }
                }
            }
            vectorRanges.put(key,list);
            return (ArrayList<Vector>)list.clone();
        }
        return (ArrayList<Vector>)vectorRanges.get(key).clone();
    }


}
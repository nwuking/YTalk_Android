package com.nwuking.ytalk;


import java.util.HashMap;
import java.util.Map;

public class HeadUtil {
    private static Map<Integer,Integer> headMaps = new HashMap<>();

    public static void put(Integer userid, Integer head) {
        headMaps.put(userid, head);
    }

    public static Integer get(Integer userid) {
        return headMaps.get(userid);
    }
}

package cn.simastudio.snippets.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenqk on 2017/5/4.
 */
public class MapDemo {

    public static void main(String[] args) {
        Map<Integer, String> nameMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            nameMap.putIfAbsent(i, "value" + i);
        }

        nameMap.forEach((id, value) -> System.out.println(value));
        nameMap.computeIfPresent(2, (id, value) -> (id + "" + value));
        System.out.println(nameMap.get(2));

        nameMap.computeIfAbsent(3, value -> value + "absent");
        System.out.println(nameMap.get(3));
        nameMap.computeIfAbsent(10, value -> value + "absent");
        System.out.println(nameMap.get(10));

        nameMap.remove(3, "value3");
        System.out.println(nameMap.containsKey(3));

        String getValue = nameMap.getOrDefault(3, "defaultValue");
        System.out.println(getValue);

        nameMap.merge(6, "newValueFor6", (value, newValue) -> value.toUpperCase().concat(newValue));
        System.out.println(nameMap.get(6));
        nameMap.merge(11, "value11", String::concat);
        System.out.println(nameMap.get(11));
    }

}

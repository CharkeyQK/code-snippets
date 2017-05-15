package cn.simastudio.snippets;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by chenqk on 2017/5/10.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        List<WeakHashMap<byte[][], byte[][]>> maps = new ArrayList<WeakHashMap<byte[][], byte[][]>>();

        for (int i = 0; i < 1000; i++) {
            WeakHashMap<byte[][], byte[][]> d = new WeakHashMap<byte[][], byte[][]>();
            d.put(new byte[1000][1000], new byte[1000][1000]);
            maps.add(d);
            System.gc();
            System.err.println(i);
        }
    }

}

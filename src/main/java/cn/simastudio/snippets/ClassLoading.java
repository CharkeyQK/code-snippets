package cn.simastudio.snippets;

import cn.simastudio.snippets.map.MapDemo;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by chenqk on 2017/5/10.
 */
public class ClassLoading {

    public static void main(String[] args) throws IOException {
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("java/lang/String.class");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>" + url.getFile());
        }

        ClassLoader cl = MapDemo.class.getClassLoader();
        for (; cl != null; cl = cl.getParent()) {
            System.out.println(cl.getClass().getName());
        }
    }
}

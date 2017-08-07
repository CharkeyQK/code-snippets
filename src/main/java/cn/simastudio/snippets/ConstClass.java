package cn.simastudio.snippets;

/**
 * Created by chenqk on 2017/7/28.
 */
public class ConstClass {

    static {
        System.out.println("ConstClass init!");
    }

    public static final String HELLOWORLD = "hello world";
    // 加上 final，表示为常量，常量会在编译时放入调用类的常量池，也就是 TestConstClass

}

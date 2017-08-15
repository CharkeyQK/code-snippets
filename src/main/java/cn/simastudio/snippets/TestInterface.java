package cn.simastudio.snippets;

/**
 * Created by chenqk on 2017/8/14.
 */
public interface TestInterface {

    public default int add(int a, int b) {
        return a + b;
    }

    public abstract int add2(int a, int b);


}

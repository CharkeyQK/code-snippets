package cn.simastudio.snippets;

/**
 * Created by chenqk on 2017/8/7.
 */
public class IntegerTest {

    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d);//1
        System.out.println(e == f);//0
        System.out.println(c == (a + b));//1
        System.out.println(c.equals(a + b));//1
        System.out.println(g == (a + b));//0    true
        System.out.println(g.equals(a +b));//0
    }

}

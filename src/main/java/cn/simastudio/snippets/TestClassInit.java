package cn.simastudio.snippets;

/**
 * Created by chenqk on 2017/7/28.
 */
public class TestClassInit {

//    静态变量、静态初始化块，变量、初始化块 初始化顺序 取决于它们在类中出现的先后顺序。

    static {
        System.out.println("static");
        i = 0;  //  给变量复制可以正常编译通过
//        System.out.print(i);  // 这句编译器会提示“非法向前引用”
    }
    static int i = 1;

    static int j = 1;

    static{
        j = 2;
    }

    public static void main(String[] args){
        System.out.println("main");
        System.out.println(TestClassInit.i);  //1
        System.out.println(TestClassInit.j); //2
    }
}

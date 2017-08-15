package cn.simastudio.snippets;

/**
 * Created by chenqk on 2017/8/15.
 */
public class NumberString2Number {

    public static void main(String[] args) {
        String str = "2112854432";
        int a = 0;
        char base = '0'; // '0' 的 十进制是 48，后续的字符  '1' - '9' 都在 48 上加 1实现
        char[] chars = str.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            a += (chars[j] - base) * getPow(10, chars.length - j - 1);
        }
        System.out.println(a);
    }

    public static int getPow(int m,int n){
        int sum = 1;
        for(int i=0;i<n;i++){
            sum = sum*m;
        }
        return sum;
    }
}

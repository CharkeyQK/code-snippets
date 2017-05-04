package cn.simastudio.snippets.predicate;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by chenqk on 2017/5/4.
 */
public class PredicateDemo {

    public static void main(String[] args) {
        Predicate<String> predicate = (a) -> (a.length() > 0);
        System.out.println(predicate.test("Charkey"));
        System.out.println(predicate.negate().test("Charkey"));

        Predicate<String> predicate1 = String::isEmpty;
        System.out.println(predicate1.test("Charkey"));
    }

}

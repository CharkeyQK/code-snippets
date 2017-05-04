package cn.simastudio.snippets.stream;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Created by chenqk on 2017/5/4.
 */
public class StreamDemo {

    /**
     * <p>terminal operation list:</p>
     * <ul>
     * <li>forEach</li>
     * <li>*Match</li>
     * <li>count</li>
     * <li>reduce</li>
     * </ul>
     *
     * @param args
     */
    public static void main(String[] args) {
        List<String> nameList = new ArrayList<>();
        nameList.add("zzz");
        nameList.add("aaa");
        nameList.add("bbb");
        nameList.add("ccc");
        nameList.add("ddd");
        nameList.add("eee");
        nameList.add("fff");

        nameList.stream().map(String::toUpperCase).forEach(System.out::println);
        System.out.println();
        nameList.stream().filter(a -> a.startsWith("a")).forEach(System.out::println);
        System.out.println();
        nameList.stream().sorted().forEach(System.out::println);
        System.out.println();
        nameList.forEach(System.out::println);
        System.out.println();

        boolean anyMatchStartWithA = nameList.stream().anyMatch(a -> a.startsWith("a"));
        System.out.println(anyMatchStartWithA);
        boolean allMatchStartWithA = nameList.stream().allMatch(a -> a.startsWith("a"));
        System.out.println(allMatchStartWithA);
        boolean noneMatchStartWithA = nameList.stream().noneMatch(a -> a.startsWith("a"));
        System.out.println(noneMatchStartWithA);

        long count = nameList.stream().filter(a -> a.startsWith("a")).count();
        System.out.println(count);

        Optional<String> reduceResult = nameList.stream().sorted().reduce((a, b) -> a + "-" + b);
        // equivalent
        Optional<String> reduceResult1 = nameList.stream().sorted().reduce(new BinaryOperator<String>() {
            @Override
            public String apply(String s, String s2) {
                return s+s2;
            }
        });

        System.out.println(reduceResult);

        Function<String, String> function = s -> s + Instant.now();
        Function<String, String> function2 = s -> s + "#";
        Function<String, String> function1 = function.andThen(function2);
        nameList.stream().map(function1).forEach(System.out::println);
    }


}

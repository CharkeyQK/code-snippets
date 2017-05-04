package cn.simastudio.snippets.optional;

import java.util.Optional;

/**
 * Created by chenqk on 2017/5/4.
 */
public class OptionalDemo {

    public static void main(String[] args) {
        Optional<String> comment = Optional.empty();
        // java.util.NoSuchElementException: No value present
        String text = "Comment: " + comment.get();
        System.out.println(text);
    }

}

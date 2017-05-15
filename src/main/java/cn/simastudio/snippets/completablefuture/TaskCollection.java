package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.TimeUnit;

/**
 * Created by chenqk on 2017/5/15.
 */
public class TaskCollection {

    public static Long doTask_100ms() {
        Long _100ms = 100L;
        try {
            TimeUnit.MILLISECONDS.sleep(_100ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _100ms;
    }

    public static Long doTask_200ms() {
        Long _200ms = 200L;
        try {
            TimeUnit.MILLISECONDS.sleep(_200ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _200ms;
    }

    public static Long doTask_300ms() {
        Long _300ms = 300L;
        try {
            TimeUnit.MILLISECONDS.sleep(_300ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _300ms;
    }

    public static Long doTask_400ms() {
        Long _400ms = 400L;
        try {
            TimeUnit.MILLISECONDS.sleep(_400ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _400ms;
    }

    public static Long doTask_500ms() {
        Long _500ms = 500L;
        try {
            TimeUnit.MILLISECONDS.sleep(_500ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _500ms;
    }

    public static Long doTask_1s() {
        Long _1s = 1L;
        try {
            TimeUnit.SECONDS.sleep(_1s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _1s * 1000;
    }

    public static Long doTask_3s() {
        Long _3s = 3L;
        try {
            TimeUnit.SECONDS.sleep(_3s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _3s * 1000;
    }

    public static Long doTask_5s() {
        Long _5s = 5L;
        try {
            TimeUnit.SECONDS.sleep(_5s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _5s * 1000;
    }
}

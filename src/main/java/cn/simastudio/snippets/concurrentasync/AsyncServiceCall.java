package cn.simastudio.snippets.concurrentasync;

import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by chenqk on 2017/5/2.
 */
public class AsyncServiceCall {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AsyncServiceCall call = new AsyncServiceCall();
        System.out.println("Async begin...");
        StopWatch watch = new StopWatch();
        for (int i = 0; i < 100; i++) {
            watch.reset();
            watch.start();
            Long result = call.getCountAsync();
            watch.stop();
            System.out.println("Result: " + result + " Time: " + watch.getTime());
        }
        System.out.println("Sync begin...");
        for (int i = 0; i < 100; i++) {
            watch.reset();
            watch.start();
            Long result = call.getCountSync();
            watch.stop();
            System.out.println("Result: " + result + " Time: " + watch.getTime());
        }
    }

    public Long getCountAsync() {
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(() -> new AsyncServiceCall().doJob1());
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(() -> new AsyncServiceCall().doJob2());
        CompletableFuture<Long> future3 = CompletableFuture.supplyAsync(() -> new AsyncServiceCall().doJob3());
        CompletableFuture future = CompletableFuture.allOf(future1, future2, future3)
                .thenApply((Long) -> {
                    try {
                        Long cnt1 = future1.get();
                        Long cnt2 = future2.get();
                        Long cnt3 = future3.get();
                        return cnt1 + cnt2 + cnt3;
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return 0L;
                    }
                })
                .exceptionally((e -> {
                    e.printStackTrace();
                    return 0L;
                }));
        try {
            return (Long) future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Long getCountSync() {
        AsyncServiceCall asyncServiceCall = new AsyncServiceCall();
        Long cnt1 = asyncServiceCall.doJob1();
        Long cnt2 = asyncServiceCall.doJob2();
        Long cnt3 = asyncServiceCall.doJob3();
        return cnt1 + cnt2 + cnt3;
    }

    public Long doJob1() {
        try {
            Thread.sleep(300L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1L;
    }

    public Long doJob2() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2L;
    }

    public Long doJob3() {
        try {
            Thread.sleep(800L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 3L;
    }
}

package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;
import java.util.function.BiFunction;

/**
 * Created by chenqk on 2017/5/2.
 */
public class CompletableFutureThenCombine {

    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(TaskCollection::doTask_100ms);
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(TaskCollection::doTask_200ms);

        // thenCombine
        // BiFunction -> apply(Long a, Long b)
        CompletableFuture<Long> resultFuture = future2.thenCombine(future1, (result1, result2) -> result1 + result2);
        try {
            System.out.println("ThenCombine result: " + resultFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // anyOf: any one is done
        CompletableFuture<Long> resultFuture2 = CompletableFuture.anyOf(future1, future2).thenApply((Long) -> {
            try {
                return future1.isDone() ? future1.get() : future2.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return 0L;
            }
        });
        try {
            System.out.println(resultFuture2.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // allOf: all done
        CompletableFuture<Long> resultFuture3 = CompletableFuture.allOf(future1, future2).thenApply((Long) -> {
            try {
                return future1.get() + future2.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return 0L;
            }
        });
        try {
            System.out.println(resultFuture3.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}

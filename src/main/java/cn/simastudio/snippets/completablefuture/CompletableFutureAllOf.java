package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by chenqk on 2017/5/16.
 */
public class CompletableFutureAllOf {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(TaskCollection::doTask_300ms);
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(TaskCollection::doTask_400ms);
        CompletableFuture<Long> future3 = CompletableFuture.supplyAsync(TaskCollection::doTask_1s);

        // 等待所有future返回
        CompletableFuture<Long> resultFuture = CompletableFuture.allOf(future1, future2, future3).thenApply(aVoid -> {
            try {
                return future1.get() + future2.get() + future3.get();
            } catch (InterruptedException | ExecutionException e) {
                return 0L;
            }
        });

        System.out.println(resultFuture.get());

        executor.shutdown();
    }

}

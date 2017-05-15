package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;
import java.util.function.BiConsumer;

/**
 * Created by chenqk on 2017/5/2.
 */
public class CompletableFutureThenAcceptBoth {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(TaskCollection::doTask_300ms);
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(TaskCollection::doTask_400ms);

        // BiFunction -> apply(Long a, Long b)
        CompletableFuture<Void> resultFuture = future1.thenAcceptBoth(future2, new BiConsumer<Long, Long>() {
            @Override
            public void accept(Long result1, Long result2) {
                System.out.println("thenAcceptBoth result:" + (result1 + result2));
            }
        });

        // just for blocking and wait resultFuture done
        System.out.println(resultFuture.get());

        executor.shutdown();
    }

}

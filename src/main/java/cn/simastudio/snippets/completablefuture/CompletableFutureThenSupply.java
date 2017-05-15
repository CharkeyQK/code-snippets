package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Created by chenqk on 2017/5/15.
 */
public class CompletableFutureThenSupply {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        // compute
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            TaskCollection.doTask_1s();
            return "Charkey";
        }, executor);

        // transform
        CompletableFuture<Integer> future2 = future1.thenApply(t -> {
            System.out.println(2);
            return t.length();
        });

        // transform
        CompletableFuture<Double> future3 = future2.thenApply(r -> r * 2.0);
        System.out.println(future3.get());

        executor.shutdown();
    }
}

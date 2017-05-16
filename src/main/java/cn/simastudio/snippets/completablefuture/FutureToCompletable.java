package cn.simastudio.snippets.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by chenqk on 2017/5/16.
 */
public class FutureToCompletable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        Future<String> future = executor.submit(() -> {
            TaskCollection.doTask_500ms();
            return "Charkey";
        });

        CompletableFuture<String> resultFuture = toCompletable(future, executor);

        System.out.println(resultFuture.get());

        executor.shutdown();
    }

    public static <T> CompletableFuture<T> toCompletable(Future<T> future, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}

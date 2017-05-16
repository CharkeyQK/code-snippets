package cn.simastudio.snippets.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by chenqk on 2017/5/16.
 */
public class CompletableFutureSequence {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(TaskCollection::doTask_300ms);
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(TaskCollection::doTask_400ms);
        CompletableFuture<Long> future3 = CompletableFuture.supplyAsync(TaskCollection::doTask_1s);

        CompletableFuture resultFuture = sequence(Arrays.asList(future1, future2, future3));

        System.out.println(resultFuture.get());

        executor.shutdown();
    }

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        // CompletableFuture.join() Returns the result value when complete, or throws an (unchecked) exception if completed exceptionally.
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
    }

}

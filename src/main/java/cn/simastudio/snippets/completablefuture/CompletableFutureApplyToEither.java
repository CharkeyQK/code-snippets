package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by chenqk on 2017/5/16.
 */
public class CompletableFutureApplyToEither {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(TaskCollection::doTask_300ms);
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(TaskCollection::doTask_400ms);

        // BiFunction -> apply(Long a, Long b)
        CompletableFuture<Long> resultFuture = future1.applyToEither(future2, new Function<Long, Long>() {
            @Override
            public Long apply(Long firstResult) {
                // 取2个future中最先返回的,有返回值
                return firstResult;
            }
        });

        // get result
        System.out.println(resultFuture.get());

        executor.shutdown();
    }

}

package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Created by chenqk on 2017/5/16.
 */
public class CompletableFutureAnyOf {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(TaskCollection::doTask_300ms);
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(TaskCollection::doTask_400ms);
        CompletableFuture<Long> future3 = CompletableFuture.supplyAsync(TaskCollection::doTask_1s);

        // 取多个future当中最快的一个返回
        CompletableFuture<Long> resultFuture = CompletableFuture.anyOf(future1, future2, future3).thenApply(new Function<Object, Long>() {
            @Override
            public Long apply(Object aLong) {
                System.out.println("first result: " + aLong);
                return (Long) aLong;
            }
        });

        System.out.println(resultFuture.get());

        executor.shutdown();
    }

}

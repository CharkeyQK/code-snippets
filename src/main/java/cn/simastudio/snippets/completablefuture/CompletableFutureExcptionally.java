package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Created by chenqk on 2017/5/16.
 */
public class CompletableFutureExcptionally {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(TaskCollection::doTask_300ms);
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(TaskCollection::doTask_400ms);
        CompletableFuture<Long> future3 = CompletableFuture.supplyAsync(CompletableFutureExcptionally::doTask_300ms_exceptionally);

        // 等待所有future返回
        CompletableFuture<Long> resultFuture = CompletableFuture.allOf(future1, future2, future3).thenApply(aVoid -> {
            try {
                return future1.get() + future2.get() + future3.get();
            } catch (InterruptedException | ExecutionException e) {
                return 0L;
            }
        }).exceptionally(new Function<Throwable, Long>() {
            @Override
            public Long apply(Throwable throwable) {
                System.out.println(throwable.getMessage());
                return 0L;
            }
        });

        System.out.println(resultFuture.get());

        executor.shutdown();
    }

    public static Long doTask_300ms_exceptionally() {
        Long _300ms = 300L;
        try {
            TimeUnit.MILLISECONDS.sleep(_300ms);
            throw new InterruptedException("exception thrown manually");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return _300ms;
    }
}

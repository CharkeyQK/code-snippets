package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by chenqk on 2017/5/16.
 */
public class CompletableFutureAcceptEither {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CompletableFuture<Long> future1 = CompletableFuture.supplyAsync(TaskCollection::doTask_300ms);
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(TaskCollection::doTask_400ms);

        // BiFunction -> apply(Long a, Long b)
        CompletableFuture<Void> resultFuture = future1.acceptEither(future2, new Consumer<Long>() {
            @Override
            public void accept(Long firstResult) {
                // 取2个future中最先返回的,无返回值
                System.out.println("first result: " + firstResult);
            }
        });

        // lambda expression
        CompletableFuture<Void> voidCompletableFuture = future1.acceptEither(future2, (result) -> {
            System.out.println("first result" + result);
        });

        // just for blocking and wait resultFuture done
        System.out.println(resultFuture.get());

        executor.shutdown();
    }

}

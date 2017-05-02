package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;

/**
 * Created by chenqk on 2017/5/2.
 */
public class CompletableFutureWithConsumer {

    public static void main(String[] args) {

        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                // execute by threadPool thread
                System.out.println(Thread.currentThread().getName());
                return "Charkey";
            } catch (InterruptedException e) {
                return "ERROR";
            }
        }, executor);

        // Consumer -> accept(String s)
        completableFuture.thenAccept(result -> {
            // execute by threadPool thread
            System.out.println(Thread.currentThread().getName());
            System.out.println("Result accepted: " + result);
        });

        // Consumer -> accept(String s)
        completableFuture
                .thenAcceptAsync(result -> {
                    // execute by threadPool thread
                    System.out.println(Thread.currentThread().getName());
                    System.out.println("Result accepted: " + result);
                })
                // Function -> apply(Throwable throwable)
                // Explicit Statement(exceptionally) to handle exceptions
                .exceptionally(throwable -> {
                    System.out.println(throwable.getMessage());
                    return null;
                })
                // throw exception: If not already completed, causes invocations of {@link #get()} and related methods to throw the given exception. Normally, no need.
                .completeExceptionally(new Exception("Error"));
        // completableFuture.thenAcceptAsync(Consumer<? super T> action, Executor executor) specify the specific executor or will use ForkJoinPool.commonPool instead.

        System.out.println("To be end; completableFuture.get() will be block until the thread finished running.");
        try {
            // submited by main thread
            System.out.println(Thread.currentThread().getName());
        } finally {
            executor.shutdown();
        }
    }

}

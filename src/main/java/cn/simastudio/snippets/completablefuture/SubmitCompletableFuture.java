package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;

/**
 * Created by chenqk on 2017/5/2.
 */
public class SubmitCompletableFuture {

    public static void main(String[] args) {
        
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                return "Charkey";
            } catch (InterruptedException e) {
                return "ERROR";
            }
        }, executor);
        System.out.println("To be end; completableFuture.get() will be block until the thread finished running.");
        try {
            System.out.println(completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

}

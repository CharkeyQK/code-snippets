package cn.simastudio.snippets.completablefuture;

import java.util.concurrent.*;

/**
 * Created by chenqk on 2017/5/2.
 */
public class SubmitFuture {

    public static void main(String[] args) throws InterruptedException {

        // Create an executor using the following code: Thread count is limited, but request queue can be Integer.MAX_VALUE
        // ExecutorService executor = Executors.newFixedThreadPool(5);

        int corePoolSize = 2;
        int maximumPoolSize = 5;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue);
        // Callable -> call()
        Future<String> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                return "Charkey";
            } catch (InterruptedException e) {
                return "ERROR";
            }
        });
        System.out.println("To be end; Future.get() will be block until the thread finished running.");
        System.out.println("Check if done? " + future.isDone());
        try {
            System.out.println(future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Check if done? " + future.isDone());
            executor.shutdown();
        }
    }

}

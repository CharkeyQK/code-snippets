package cn.simastudio.snippets.concurrentasync;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.CompletableFuture;

/**
 * Created by chenqk on 2017/5/2.
 */
public class AsyncTest {

    static CloseableHttpAsyncClient asyncHttpClient = HttpAsyncClients.createDefault();
    public static CompletableFuture<String> getHttpData(String url) {
        CompletableFuture asyncFuture = new CompletableFuture();
        HttpPost post = new HttpPost(url);
        HttpAsyncRequestProducer producer = HttpAsyncMethods.create(post);
        AsyncCharConsumer<HttpResponse> consumer = new AsyncCharConsumer<HttpResponse>() {
            @Override
            protected void onCharReceived(CharBuffer charBuffer, IOControl ioControl) throws IOException {
                System.out.println(charBuffer);
            }

            HttpResponse response;

            @Override
            protected void onResponseReceived(HttpResponse httpResponse) throws HttpException, IOException {

            }

            protected HttpResponse buildResult(final HttpContext context) {
            return response;
        }
       };
        FutureCallback callback = new FutureCallback<HttpResponse>() {
            public void completed(HttpResponse response) {
                try {
                    asyncFuture.complete(EntityUtils.toString(response.getEntity()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void cancelled() {

            }
        };
        asyncHttpClient.start();
        asyncHttpClient.execute(producer, consumer, callback);
        return asyncFuture;
    }

    public static void main(String[] args) throws Exception {
        AsyncTest.getHttpData("http://www.jd.com");
        Thread.sleep(1000);
        asyncHttpClient.close();
    }

}

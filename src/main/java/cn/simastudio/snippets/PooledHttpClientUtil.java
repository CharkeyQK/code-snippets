package cn.simastudio.snippets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PooledHttpClientUtil {

//    private static Logger logger = LoggerFactory.getLogger(PooledHttpClientUtil.class);

    private static PoolingHttpClientConnectionManager manager = null;

    private static CloseableHttpClient httpClient = null;

    /**
     * 连接超时时间
     */
    private static Integer CONN_TIMEOUT = 2 * 1000;
    /**
     * 等待数据超时时间
     */
    private static Integer SO_TIMEOUT = 5 * 1000;
    /**
     * 从连接池获取连接的等待超时时间
     */
    private static Integer CONN_REQ_TIMEOUT = 2 * 1000;

    private final static Object syncLock = new Object();

    /**
     * 获取HttpClient对象
     */
    private static CloseableHttpClient getHttpClient(String url) {
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = createHttpClient(300, 50, 100, hostname, port);
                }
            }
        }
        return httpClient;
    }

    /**
     * 创建HttpClient对象
     */
    private static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname, int port) {
        // 注册访问协议相关的 Socket 工厂
        ConnectionSocketFactory plainSf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslSf = SSLConnectionSocketFactory.getSystemSocketFactory();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainSf)
                .register("https", sslSf)
                .build();
        // HttpConnection 工厂：配置写请求/解析响应处理器
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory =
                new ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE,
                        DefaultHttpResponseParserFactory.INSTANCE);
        // DNS 解析器
        DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
        // 创建池化连接管理器
        manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);
        // 默认为 Socket 配置
        SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        manager.setDefaultSocketConfig(defaultSocketConfig);

        // 设置整个连接池的最大连接数
        manager.setMaxTotal(maxTotal);
        // 设置每个路由的默认最大连接数
        // 每个路由实际最大连接数默认为 DefaultMaxPerRoute 控制，而 MaxTotal 是控制整个池子最大数；
        // 设置过小无法支持大并发（ConnectionPoolTimeoutException: Timeout waiting for connection from pool）,路由是对 maxTotal 的细分
        manager.setDefaultMaxPerRoute(maxPerRoute);

        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        manager.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为 2s
        manager.setValidateAfterInactivity(5 * 1000);
        // 默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(CONN_TIMEOUT)    // 设置连接超时时间
                .setSocketTimeout(SO_TIMEOUT)     // 设置等待数据超时时间
                .setConnectionRequestTimeout(CONN_REQ_TIMEOUT)      // 设置从连接池获取连接的等待超时时间
                .build();

        // 创建 HttpClient
        httpClient = HttpClients.custom()
                .setConnectionManager(manager)      // 设置池化连接管理器
                .setConnectionManagerShared(false)      // 设置连接池不是共享模式
                .evictIdleConnections(60, TimeUnit.SECONDS)      // 设置定期回收空闲连接
                .evictExpiredConnections()      // 设置定期回收过期连接
                .setConnectionTimeToLive(60, TimeUnit.SECONDS)      // 设置连接存活时间，如果不设置，则根据长连接信息决定
                .setDefaultRequestConfig(defaultRequestConfig)      // 设置默认请求配置
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)      // 设置连接重用策略，即是否能 keepAlive
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)      // 设置长连接配置，即获取长连接生产多长时间
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))      // 设置重试处理，当前禁用
                .build();
        // 返回
        return httpClient;
    }

    /**
     * GET请求URL获取内容
     */
    public static String get(String url) throws IOException {
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httpget, HttpClientContext.create());
            return getEntityContent(response);
        } catch (IOException e) {
//            logger.error("HTTP GET execute error: {}", e.getMessage());
            if (response != null) {
                EntityUtils.consume(response.getEntity());
            }
        }
        return null;
    }

    /**
     */
    public static String post(String url, Map<String, Object> params) throws IOException {
        HttpPost httppost = new HttpPost(url);
        setPostParams(httppost, params);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httppost, HttpClientContext.create());
            return getEntityContent(response);
        } catch (Exception e) {
//            logger.error("HTTP POST execute error: {}", e.getMessage());
            if (response != null) {
                EntityUtils.consume(response.getEntity());
            }
        }
        return null;
    }

    /**
     */
    public static String post(String url, String postContent) throws IOException {
        HttpPost httppost = new HttpPost(url);
        StringEntity entity = new StringEntity(postContent, Charset.defaultCharset());
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httppost, HttpClientContext.create());
            return getEntityContent(response);
        } catch (Exception e) {
//            logger.error("HTTP POST execute error: {}", e.getMessage());
            if (response != null) {
                EntityUtils.consume(response.getEntity());
            }
        }
        return null;
    }

    private static String getEntityContent(CloseableHttpResponse response) throws IOException {
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
//            logger.error("get entity content error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 设置 POST 请求参数：UrlEncodedFormEntity
     *
     * @param httpPost HttpPost
     * @param params   Map 形式的参数
     */
    private static void setPostParams(HttpPost httpPost, Map<String, Object> params) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nameValuePairs.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // URL列表数组
        String[] urisToGet = {
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497"};

        long start = System.currentTimeMillis();
        try {
            int pagecount = urisToGet.length;
            ExecutorService executors = Executors.newFixedThreadPool(pagecount);
            CountDownLatch countDownLatch = new CountDownLatch(pagecount);
            for (int i = 0; i < pagecount; i++) {
                HttpGet httpget = new HttpGet(urisToGet[i]);
                // 启动线程抓取
                executors.execute(new GetRunnable(urisToGet[i], countDownLatch));
            }
            countDownLatch.await();
            executors.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("线程" + Thread.currentThread().getName() + ","
                    + System.currentTimeMillis() + ", 所有线程已完成，开始进入下一步！");
        }

        long end = System.currentTimeMillis();
        System.out.println("consume -> " + (end - start));
    }

    static class GetRunnable implements Runnable {
        private CountDownLatch countDownLatch;
        private String url;

        public GetRunnable(String url, CountDownLatch countDownLatch) {
            this.url = url;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                PooledHttpClientUtil.get(url);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }

}

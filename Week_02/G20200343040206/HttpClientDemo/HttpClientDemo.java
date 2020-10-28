package HttpClientDemo;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.util.Map;


public class HttpClientDemo {
    private static CloseableHttpClient httpClient;

    static {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", new SSLConnectionSocketFactory(builder.build()))
                    .build();

            PoolingHttpClientConnectionManager poolConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolConnectionManager.setMaxTotal(100);
            poolConnectionManager.setDefaultMaxPerRoute(100);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000)
                    .setSocketTimeout(3000)
                    .setConnectTimeout(3000)
                    .build();

            httpClient = HttpClients.custom()
                    .setConnectionManager(poolConnectionManager)
                    .setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                    .build();




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String get(String url, Map<String, Object> params, Map<String, String> headers) {
        StringBuilder request = new StringBuilder(url);
        if (params != null) {
            request.append("?");
            params.entrySet().forEach(entry -> {
                request.append(entry.getKey()).append("=").append(entry.getValue());
                request.append("&");
            });
        }

        HttpRequestBase httpRequestBase = new HttpGet(request.toString());

        if (headers != null) {
            headers.entrySet().forEach(k -> {
                httpRequestBase.addHeader(k.getKey(), k.getValue());
            });
        }

        String result = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpRequestBase);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }


}

package com.richard.configuration;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by richard on 22/07/2016.
 */
@Configuration
public class RestTemplateConfig {

    public static HttpClient internalHttpClient() throws URISyntaxException {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(100).setConnectionRequestTimeout(1000).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(20);
        return HttpClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
    }

    public static ClientHttpRequestFactory internalHttpRequestFactory() throws URISyntaxException {
        return new HttpComponentsClientHttpRequestFactory(internalHttpClient());
    }

    @Bean
    public RestTemplate inthubRestTemplate() throws URISyntaxException {
        RestTemplate template = new RestTemplate(this.internalHttpRequestFactory());

        template.getInterceptors().add(new ClientHttpRequestInterceptor() {
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().set("content-type","application/json");
                request.getHeaders().set("Authorization", "Basic M3YwNmk6QUJDMTIz");
                return execution.execute(request, body);
            }
        });

        return template;
    }
}

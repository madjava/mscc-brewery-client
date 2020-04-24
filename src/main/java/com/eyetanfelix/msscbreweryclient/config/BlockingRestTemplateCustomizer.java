package com.eyetanfelix.msscbreweryclient.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

    private final Integer maxTotalConnections;
    private final Integer defaultTotalConnections;
    private final Integer connectionTimeout;
    private final Integer socketTimeout;

    public BlockingRestTemplateCustomizer(@Value("${sfg.maxtotalconnections}") Integer maxTotalConnections,
                                          @Value("${sfg.defaulttotalconnections}") Integer defaultTotalConnections,
                                          @Value("${sfg.connectiontimeout}") Integer connectionTimeout,
                                          @Value("${sfg.sockettimeout}") Integer socketTimeout) {
        this.maxTotalConnections = maxTotalConnections;
        this.defaultTotalConnections = defaultTotalConnections;
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(defaultTotalConnections);

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}

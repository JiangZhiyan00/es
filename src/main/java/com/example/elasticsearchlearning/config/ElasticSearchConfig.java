package com.example.elasticsearchlearning.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ElasticSearchConfig {

    @Value("${spring.data.elasticsearch.scheme:http}")
    private String scheme;
    @Value("${spring.data.elasticsearch.cluster-nodes:127.0.0.1:9200}")
    private String[] ipAddress;
    @Value("${spring.data.elasticsearch.connection-request-timeout}")
    private Integer connectionRequestTimeout;
    @Value("${spring.data.elasticsearch.socket-timeout}")
    private Integer socketTimeout;
    @Value("${spring.data.elasticsearch.connect-timeout}")
    private Integer connectTimeout;

    @Bean
    RestHighLevelClient getRestHighLevelClient(){
        List<HttpHost> hosts = new ArrayList<>();
        for (String address : ipAddress) {
            HttpHost httpHost = this.getHttpHost(address);
            hosts.add(httpHost);
        }
        RestClientBuilder restClientBuilder = RestClient.builder(hosts.toArray(new HttpHost[0]));
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout));
        return new RestHighLevelClient(restClientBuilder);
    }

    private HttpHost getHttpHost(String address){
        String[] split = address.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        return new HttpHost(ip, port, scheme);
    }
}

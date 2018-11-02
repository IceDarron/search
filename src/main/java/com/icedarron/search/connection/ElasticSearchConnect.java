package com.icedarron.search.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
@PropertySource("classpath:application.yml")
public final class ElasticSearchConnect {

    // 日志
    private final static Logger LOGGER = LogManager.getLogger(ElasticSearchConnect.class);

    // 该连接单利模式
    private volatile static ElasticSearchConnect _instance = null;

    // 连接客户端
    private TransportClient transportClient;

    // es配置信息
    @Value("${elasticsearch.clusterName}")
    private String clusterName;
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.poolSize}")
    private Integer poolSize;

    @Bean(name = "transportClient")
    public TransportClient transportClient() {
        try {
            LOGGER.info("初始化elasticsearch连接！");
            Settings esSettings = Settings.builder()
                    .put("cluster.name", clusterName) //集群名字
                    .put("client.transport.sniff", true)//增加嗅探机制，找到ES集群
                    .put("thread_pool.search.size", poolSize)//增加线程池个数，暂时设为5
                    .build();

            transportClient = new PreBuiltTransportClient(esSettings);
            transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
            LOGGER.info("连接elasticsearchsearch成功！");
        } catch (Exception e) {
            LOGGER.error("连接elasticsearch错误：{}", e.getMessage(), e);
        }
        return transportClient;
    }
}

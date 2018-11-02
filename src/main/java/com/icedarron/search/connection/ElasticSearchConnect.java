package com.icedarron.search.connection;

import com.icedarron.search.util.ConfigUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public final class ElasticSearchConnect {

    // 日志
    private final static Logger LOGGER = LoggerFactory.getLogger(ElasticSearchConnect.class);

    // 该连接单利模式
    private volatile static ElasticSearchConnect _instance = null;

    // 连接客户端
    private TransportClient client;

    // es配置信息
    private String clusterName = null;
    private String host = null;
    private Integer port = null;
    private InetAddress address = null;
    private Integer poolSize = null;

    /**
     * 私有构造函数
     */
    private ElasticSearchConnect() {
        try {
            LOGGER.info("初始化elasticsearch连接！");
            clusterName = ConfigUtils.ELASTICSEARCH_CLUSTER_NAME;
            host = ConfigUtils.ELASTICSEARCH_HOST;
            port = ConfigUtils.ELASTICSEARCH_PORT;
            address = InetAddress.getByName(host);
            poolSize = ConfigUtils.ELASTICSEARCH_CLIENT_POOLSIZE;

            Settings esSettings = Settings.builder()
                    .put("cluster.name", clusterName) //集群名字
                    .put("client.transport.sniff", true)//增加嗅探机制，找到ES集群
                    .put("thread_pool.search.size", poolSize)//增加线程池个数，暂时设为5
                    .build();


            client = new PreBuiltTransportClient(esSettings);
            client.addTransportAddress(new TransportAddress(address, port));
            LOGGER.info("连接elasticsearchsearch成功！");
        } catch (Exception e) {
            LOGGER.error("连接elasticsearch错误：{}", e.getMessage(), e);
        }
    }

    /**
     * 获取连接客户端
     *
     * @return
     */
    public TransportClient getClient() {
        return client;
    }


    /**
     * 获取实例
     *
     * @return
     */
    public static ElasticSearchConnect getInstance() {
        if (null == _instance) {
            synchronized (ElasticSearchConnect.class) {
                if (null == _instance) {
                    _instance = new ElasticSearchConnect();
                }
            }
        }
        return _instance;
    }
}

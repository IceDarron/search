package com.icedarron.search.connection;

import com.icedarron.search.util.ConfigUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class ElasticSearchConnect {

    // 日志
    private final static Logger logger = LoggerFactory.getLogger(ElasticSearchConnect.class);
    //
    private volatile static ElasticSearchConnect _instance = null;
    // 连接客户端
    private TransportClient client;

    /**
     * 私有构造函数
     */
    private ElasticSearchConnect() {
        try {
            String clusterName = ConfigUtils.ELASTICSEARCH_CLUSTER_NAME;
            String host = ConfigUtils.ELASTICSEARCH_HOST;
            int port = ConfigUtils.ELASTICSEARCH_PORT;
            InetAddress address = InetAddress.getByName(host);

            Settings settings = Settings.builder().put("cluster.name", clusterName)
                    .put("xpack.security.user", "elastic:elastic")
                    .put("client.transport.sniff", true).build();
            client = new PreBuiltXPackTransportClient(settings);
            client.addTransportAddress(new TransportAddress(address, port));
            logger.info("连接elasticsearchsearch成功！");
        } catch (Exception e) {
            logger.error("连接elasticsearch错误：{}", e.getMessage(), e);
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

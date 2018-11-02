package com.icedarron.search.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class ConfigUtils {

    @Value("${elasticsearch.cluster_name}")
    public static final String ELASTICSEARCH_CLUSTER_NAME = null;
    @Value("${elasticsearch.host}")
    public static final String ELASTICSEARCH_HOST = null;
    @Value("${elasticsearch.port}")
    public static final Integer ELASTICSEARCH_PORT = null;
    @Value("${elasticsearch.poolSize}")
    public static final Integer ELASTICSEARCH_CLIENT_POOLSIZE = null;

}

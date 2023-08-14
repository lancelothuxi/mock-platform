package com.lancelot.mock.api.controller.cache;

import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/7/18 下午4:36
 */
public class RedissonClusterPubSubExample {

    public static void main(String[] args) {
        // Create Redisson client with cluster mode configuration
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress("redis://10.213.50.162:7000", "redis://10.213.50.163:7000");
        RedissonClient redisson = Redisson.create(config);

        // Get the topic instance
        RTopic topic = redisson.getTopic("myTopic");

        // Subscribe to the topic
        topic.addListener(String.class, (channel, message) -> {
            System.out.println("Received message: " + message);
        });

        // Publish a message to the topic
        topic.publish("Hello, Redisson in cluster mode!");

        // Shutdown Redisson client
        redisson.shutdown();
    }
}

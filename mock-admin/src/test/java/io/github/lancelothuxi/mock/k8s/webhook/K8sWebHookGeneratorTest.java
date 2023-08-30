package io.github.lancelothuxi.mock.k8s.webhook;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.junit.jupiter.api.Test;


/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/30 上午11:53
 */
@EnableKubernetesMockClient
public class K8sWebHookGeneratorTest {

    KubernetesMockServer server;
    KubernetesClient client;

    @Test
    public void testCreateWebhook() {

        K8sWebHookGenerator k8sWebHookGenerator=new K8sWebHookGenerator();
        k8sWebHookGenerator.createWebhook();

    }
}
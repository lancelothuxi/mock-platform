package io.github.lancelothuxi.mock.k8s.webhook;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookBuilder;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookConfiguration;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookConfigurationBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;


/**
 * @author lancelot
 * @version 1.0
 * @since 2023/8/14 下午7:33
 */
public class K8sWebHookGenerator {

    KubernetesClient client = new DefaultKubernetesClient();


    public void createWebhook(){
        MutatingWebhookConfiguration webhookConfiguration = new MutatingWebhookConfigurationBuilder()
                .withNewMetadata().withName("mutatingWebhookConfiguration1").endMetadata()
                .addToWebhooks(new MutatingWebhookBuilder()
                        .withName("webhook1")
                        .withNewClientConfig()
                        .withNewService()
                        .withName("svc1")
                        .withNamespace("test")
                        .withPath("/mutate")
                        .endService()
                        .endClientConfig()
                        .build())
                .build();


        client.admissionRegistration().v1().mutatingWebhookConfigurations().create(webhookConfiguration);
    }
}

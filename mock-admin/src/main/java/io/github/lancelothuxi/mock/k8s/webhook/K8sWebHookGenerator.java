package io.github.lancelothuxi.mock.k8s.webhook;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookBuilder;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookConfiguration;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookConfigurationBuilder;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.RuleWithOperationsBuilder;
import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequest;
import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequestBuilder;
import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequestCondition;
import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequestConditionBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;


/**
 * @author lancelot
 * @version 1.0
 * @since 2023/8/14 下午7:33
 */
public class K8sWebHookGenerator {

    KubernetesClient client = new DefaultKubernetesClient();

    /**
     * 创建一个MutatingWebhookConfiguration
     */
    public void createWebhook(){

        Map<String,String>  labelMap =new HashMap<>();
        labelMap.put("mock-agent-enabled","true");
        // Create a LabelSelector to match namespaces with specific labels
        LabelSelector namespaceSelector = new LabelSelectorBuilder()
                .withMatchLabels(labelMap)
                .build();

        MutatingWebhookConfiguration webhookConfiguration = new MutatingWebhookConfigurationBuilder()
                .withNewMetadata().withName("mock-agent-mutation-webhook").endMetadata()
                .addToWebhooks(new MutatingWebhookBuilder()
                        .withName("mock-agent-mutation-webhook")
                        .withSideEffects("None")
                        .withAdmissionReviewVersions(Arrays.asList("v1"))
                        .withNewClientConfig()
                        .withUrl("")
//                        .withNewService()
//                        .withName("svc1")
//                        .withNamespace("test")
//                        .withPath("/mutate")
//                        .endService()
                        .endClientConfig()
                        .withRules(new RuleWithOperationsBuilder()
                                .withOperations("CREATE")
                                .withApiGroups("apps")
                                .withApiVersions("v1")
                                .withResources("deployments")
                                .build())
                        .withNamespaceSelector(namespaceSelector)
                        .build())
                .build();


        client.admissionRegistration().v1()
                .mutatingWebhookConfigurations().replace(webhookConfiguration);

    }

    /**
     *
     */
    public void deleteWebhook(){

        boolean hasMatchingWebhook = new MutatingWebhookConfigurationBuilder()
                .hasMatchingWebhook(new Predicate<MutatingWebhookBuilder>() {
                    @Override
                    public boolean test(MutatingWebhookBuilder mutatingWebhookBuilder) {
                        return mutatingWebhookBuilder.getName().equals("mock-agent-mutation-webhook");
                    }
                });
        if(!hasMatchingWebhook){
            return;
        }

        client.admissionRegistration().v1()
                .mutatingWebhookConfigurations().delete(new MutatingWebhookConfigurationBuilder().withNewMetadata()
        .withName("mock-agent-mutation-webhook").endMetadata().build());

    }


    public void selfApproveCSR(){

        CertificateSigningRequest csr = new CertificateSigningRequestBuilder()
                .withNewMetadata()
                .withName("your-csr-name")
                .endMetadata()
                .withNewSpec()
                .withRequest(Base64.getEncoder().encodeToString(new byte[]{}))
                .endSpec()
                .build();

        CertificateSigningRequest certificateSigningRequest = client.certificates().v1().certificateSigningRequests().resource(csr).create();

        CertificateSigningRequestCondition csrCondition = new CertificateSigningRequestConditionBuilder()
                .withType("Approved")
                .withStatus("True")
                .withReason("ApprovedViaRESTApi")
                .withMessage("Approved by REST API /approval endpoint.")
                .build();
        client.certificates().v1().certificateSigningRequests().withName("my-cert").approve(csrCondition);

    }

}

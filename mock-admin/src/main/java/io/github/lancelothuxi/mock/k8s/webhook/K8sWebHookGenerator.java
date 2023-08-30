package io.github.lancelothuxi.mock.k8s.webhook;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookBuilder;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookConfiguration;
import io.fabric8.kubernetes.api.model.admissionregistration.v1.MutatingWebhookConfigurationBuilder;
import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequest;
import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequestBuilder;
import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequestCondition;
import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequestConditionBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.Arrays;
import java.util.Base64;


/**
 * @author lancelot
 * @version 1.0
 * @since 2023/8/14 下午7:33
 */
public class K8sWebHookGenerator {

    KubernetesClient client = new DefaultKubernetesClient();

    public void createWebhook(){
        MutatingWebhookConfiguration webhookConfiguration = new MutatingWebhookConfigurationBuilder()
                .withNewMetadata().withName("ggg").endMetadata()
                .addToWebhooks(new MutatingWebhookBuilder()
                        .withName("com.lancelot.com")
                        .withSideEffects("None")
                        .withAdmissionReviewVersions(Arrays.asList("v1"))
                        .withNewClientConfig()
                        .withNewService()
                        .withName("svc1")
                        .withNamespace("test")
                        .withPath("/mutate")
                        .endService()
                        .endClientConfig()
                        .build())
                .build();


        client.admissionRegistration().v1()
                .mutatingWebhookConfigurations().delete(webhookConfiguration);

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

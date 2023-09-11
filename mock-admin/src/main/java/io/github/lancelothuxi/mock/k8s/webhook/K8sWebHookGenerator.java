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
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.github.lancelothuxi.mock.domain.KubeConfig;
import io.github.lancelothuxi.mock.mock.MockDubboServiceImpl;
import io.github.lancelothuxi.mock.service.IKubeConfigService;
import io.github.lancelothuxi.mock.ssl.PEMImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lancelot
 * @version 1.0
 * @since 2023/8/14 下午7:33
 */
@Service
public class K8sWebHookGenerator {

  private static Logger logger = LoggerFactory.getLogger(MockDubboServiceImpl.class);

  @Autowired
  private IKubeConfigService kubeConfigService;

  KubernetesClient client = new DefaultKubernetesClient();

  private static String encodeBase64(String content) {
    byte[] encodedBytes = Base64.getEncoder().encode(content.getBytes(StandardCharsets.UTF_8));
    return new String(encodedBytes, StandardCharsets.UTF_8);
  }

  /** 创建一个MutatingWebhookConfiguration */
  public void createWebhook(KubeConfig kubeConfig) {

    client= new KubernetesClientBuilder()
            .withConfig(kubeConfig.getContent())
            .build();

    Map<String, String> labelMap = new HashMap<>();
    labelMap.put("mock-agent-enabled", "true");
    // Create a LabelSelector to match namespaces with specific labels
    LabelSelector namespaceSelector = new LabelSelectorBuilder().withMatchLabels(labelMap).build();

    MutatingWebhookConfiguration webhookConfiguration =
        new MutatingWebhookConfigurationBuilder()
            .withNewMetadata()
            .withName("mock-agent-mutation-webhook")
            .endMetadata()
            .addToWebhooks(
                new MutatingWebhookBuilder()
                    .withName("io.github.lancelothuxi.mockagent")
                    .withSideEffects("None")
                    .withAdmissionReviewVersions(Arrays.asList("v1"))
                    .withNewClientConfig()
                    .withUrl("https://localhost:8080/mutate")
                    .endClientConfig()
                    .withRules(
                        new RuleWithOperationsBuilder()
                            .withOperations("CREATE")
                            .withApiGroups("apps")
                            .withApiVersions("v1")
                            .withResources("deployments")
                            .build())
                    .withNamespaceSelector(namespaceSelector)
                    .build())
            .build();

    client
        .admissionRegistration()
        .v1()
        .mutatingWebhookConfigurations()
        .createOrReplace(webhookConfiguration);
  }

  /** */
  public void deleteWebhook() {
    client
        .admissionRegistration()
        .v1()
        .mutatingWebhookConfigurations()
        .delete(
            new MutatingWebhookConfigurationBuilder()
                .withNewMetadata()
                .withName("mock-agent-mutation-webhook")
                .endMetadata()
                .build());
  }

  /** */
  public void selfApproveCSR(String certificatePem, String privatePem, String csrName)
      throws Exception {

    String csrContent = PEMImporter.convertCertToCSR(certificatePem, privatePem);
    String csrEncoded = encodeBase64(csrContent);

    CertificateSigningRequest csr =
        new CertificateSigningRequestBuilder()
            .withNewMetadata()
            .withName(csrName)
            .endMetadata()
            .withNewSpec()
            .withRequest(csrEncoded)
            .withSignerName("kubernetes.io/kube-apiserver-client")
            .withUsages("client auth")
            .endSpec()
            .withApiVersion("v1")
            .build();

    client.certificates().v1().certificateSigningRequests().resource(csr).create();

    CertificateSigningRequestCondition csrCondition =
        new CertificateSigningRequestConditionBuilder()
            .withType("Approved")
            .withStatus("True")
            .withReason("ApprovedViaRESTApi")
            .withMessage("Approved by REST API /approval endpoint.")
            .build();
    client.certificates().v1().certificateSigningRequests().withName(csrName).approve(csrCondition);
  }
}

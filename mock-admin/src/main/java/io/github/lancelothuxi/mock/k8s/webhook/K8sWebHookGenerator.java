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
import io.github.lancelothuxi.mock.mock.MockDubboServiceImpl;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.io.*;


/**
 * @author lancelot
 * @version 1.0
 * @since 2023/8/14 下午7:33
 */
public class K8sWebHookGenerator {

    private static Logger logger= LoggerFactory.getLogger(MockDubboServiceImpl.class);

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
                        .withName("io.github.lancelothuxi.mockagent")
                        .withSideEffects("None")
                        .withAdmissionReviewVersions(Arrays.asList("v1"))
                        .withNewClientConfig()
                        .withUrl("https://localhost:8080/mutate")
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
                .mutatingWebhookConfigurations().createOrReplace(webhookConfiguration);

    }

    /**
     *
     */
    public void deleteWebhook(){
        client.admissionRegistration().v1()
                .mutatingWebhookConfigurations().delete(new MutatingWebhookConfigurationBuilder().withNewMetadata()
        .withName("mock-agent-mutation-webhook").endMetadata().build());

    }




    /**
     *
     */
    public void selfApproveCSR(File certificatePemFile){

        String csrYaml = getCsrYaml(certificatePemFile);

        CertificateSigningRequest csr = new CertificateSigningRequestBuilder()
                .withNewMetadata()
                .withName("mock-agent-csr")
                .endMetadata()
                .withNewSpec()
                .withRequest(csrYaml)
                .endSpec()
                .build();

        CertificateSigningRequest certificateSigningRequest = client.certificates().v1().certificateSigningRequests().resource(csr).create();

        CertificateSigningRequestCondition csrCondition = new CertificateSigningRequestConditionBuilder()
                .withType("Approved")
                .withStatus("True")
                .withReason("ApprovedViaRESTApi")
                .withMessage("Approved by REST API /approval endpoint.")
                .build();
        client.certificates().v1().certificateSigningRequests().withName("mock-agent-csr").approve(csrCondition);

    }


    public String getCsrYaml(File certificatePemFile){
        try {
            // 读取PEM证书内容
            String pemContent = FileUtils.readFileToString(certificatePemFile, Charset.defaultCharset());

            // 对PEM证书内容进行Base64编码
            String base64EncodedCertificate = Base64.getEncoder().encodeToString(pemContent.getBytes());

            // 创建CSR YAML文件
            String csrYaml = "apiVersion: certificates.k8s.io/v1beta1\n" +
                    "kind: CertificateSigningRequest\n" +
                    "metadata:\n" +
                    "  name: mock-agent-csr\n" +
                    "spec:\n" +
                    "  request: " + base64EncodedCertificate + "\n" +
                    "  signerName: <ca-signer-name>\n" +
                    "  usages:\n" +
                    "  - digital signature\n" +
                    "  - key encipherment\n" +
                    "  - server auth";

            return csrYaml;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}

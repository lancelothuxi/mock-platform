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
import io.github.lancelothuxi.mock.ssl.PEMImporter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void selfApproveCSR(String certificatePem,String privatePem,String csrName) throws Exception{

        String csrContent = PEMImporter.convertCertToCSR(certificatePem, privatePem);
        String csrEncoded = encodeBase64(csrContent);

        CertificateSigningRequest csr = new CertificateSigningRequestBuilder()
                .withNewMetadata()
                .withName(csrName)
                .endMetadata()
                .withNewSpec()
                .withRequest(csrEncoded)
                .withSignerName("kubernetes.io/kube-apiserver-client")
                .withUsages(
                    "client auth"
                )
                .endSpec()
                .withApiVersion("v1")
                .build();

        client.certificates().v1().certificateSigningRequests().resource(csr).create();

        CertificateSigningRequestCondition csrCondition = new CertificateSigningRequestConditionBuilder()
                .withType("Approved")
                .withStatus("True")
                .withReason("ApprovedViaRESTApi")
                .withMessage("Approved by REST API /approval endpoint.")
                .build();
        client.certificates().v1().certificateSigningRequests().withName(csrName).approve(csrCondition);

    }


    public String getCsrYaml(String csrContent){
        // 读取PEM证书内容
//        byte[] pemContent = csrContent.getBytes();

        // 对PEM证书内容进行Base64编码
//        String base64EncodedCertificate = Base64.getEncoder().encodeToString(pemContent);
        String base64EncodedCertificate="LS0tLS1CRUdJTiBDRVJUSUZJQ0FURSBSRVFVRVNULS0tLS0KTUlJQ1ZqQ0NBVDRDQVFBd0VURVBNQTBHQTFVRUF3d0dZVzVuWld4aE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRgpBQU9DQVE4QU1JSUJDZ0tDQVFFQTByczhJTHRHdTYxakx2dHhWTTJSVlRWMDNHWlJTWWw0dWluVWo4RElaWjBOCnR2MUZtRVFSd3VoaUZsOFEzcWl0Qm0wMUFSMkNJVXBGd2ZzSjZ4MXF3ckJzVkhZbGlBNVhwRVpZM3ExcGswSDQKM3Z3aGJlK1o2MVNrVHF5SVBYUUwrTWM5T1Nsbm0xb0R2N0NtSkZNMUlMRVI3QTVGZnZKOEdFRjJ6dHBoaUlFMwpub1dtdHNZb3JuT2wzc2lHQ2ZGZzR4Zmd4eW8ybmlneFNVekl1bXNnVm9PM2ttT0x1RVF6cXpkakJ3TFJXbWlECklmMXBMWnoyalVnald4UkhCM1gyWnVVV1d1T09PZnpXM01LaE8ybHEvZi9DdS8wYk83c0x0MCt3U2ZMSU91TFcKcW90blZtRmxMMytqTy82WDNDKzBERHk5aUtwbXJjVDBnWGZLemE1dHJRSURBUUFCb0FBd0RRWUpLb1pJaHZjTgpBUUVMQlFBRGdnRUJBR05WdmVIOGR4ZzNvK21VeVRkbmFjVmQ1N24zSkExdnZEU1JWREkyQTZ1eXN3ZFp1L1BVCkkwZXpZWFV0RVNnSk1IRmQycVVNMjNuNVJsSXJ3R0xuUXFISUh5VStWWHhsdnZsRnpNOVpEWllSTmU3QlJvYXgKQVlEdUI5STZXT3FYbkFvczFqRmxNUG5NbFpqdU5kSGxpT1BjTU1oNndLaTZzZFhpVStHYTJ2RUVLY01jSVUyRgpvU2djUWdMYTk0aEpacGk3ZnNMdm1OQUxoT045UHdNMGM1dVJVejV4T0dGMUtCbWRSeEgvbUNOS2JKYjFRQm1HCkkwYitEUEdaTktXTU0xMzhIQXdoV0tkNjVoVHdYOWl4V3ZHMkh4TG1WQzg0L1BHT0tWQW9FNkpsYWFHdTlQVmkKdjlOSjVaZlZrcXdCd0hKbzZXdk9xVlA3SVFjZmg3d0drWm89Ci0tLS0tRU5EIENFUlRJRklDQVRFIFJFUVVFU1QtLS0tLQo=";
        // 创建CSR YAML文件
        String csrYaml = "apiVersion: certificates.k8s.io/v1\n" +
                "kind: CertificateSigningRequest\n" +
                "metadata:\n" +
                "  name: my-cert23330000\n" +
                "spec:\n" +
                "  groups:\n" +
                "    - system:authenticated\n" +
                "  request: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURSBSRVFVRVNULS0tLS0KTUlJQzJEQ0NBY0FDQVFBd2dZMHhDekFKQmdOVkJBWVRBa05PTVJJd0VBWURWUVFJREFsSGRXRnVaMlJ2Ym1jeApFakFRQmdOVkJBY01DVWQxWVc1bklGTjBjbUYwWlRFUE1BMEdBMVVFQ2d3R1JHVjJaV3h2Y0cxbGJuUXhDekFKCkJnTlZCQXNNQWtsVU1Sc3dHUVlEVlFRRERCSmtaWFpsYkc5d2JXVnVkQzVqYjIwZ1JYaGhiWEJzWlRFaE1COEcKQ1NxR1NJYjNEUUVKQVJZU2JXRnBiRUJsZUdGdGNHeGxMbU52YlRDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRApnZ0VQQURDQ0FRb0NnZ0VCQUswUnlKZzN1NXNIYlhqNmoyUUlqMkVKM0MrSDMrUUg1cTlQakY5WlAxTDRXZGFTCmZqKzNhQ3FTOVdqVHowT3lmLzZyV1U2cko0cjUvOGQ2VnZHZ0tvWlFxSlExbFoxT2VKMEUxdVkwb1hHWTNRVngKa1cyRjFvVVo1bVl4TDBLdFhzMnJTMkxYUTVuM3RaYTB3M1cxdEwzUzBiTDZQNnczUG1UUW1KZkVpNVl0WWwzeApYYThLeDZYN3EwcjNUSjh0WjRkVW9MOHF6MStVOWZaanZ6WWpKYzJ6SHhHdkYyVTJ3M0puN2UwZFZLNjl5RzZwClIvN2VMOWxXZzJVOFFmTDh1UUo3bVM4T3Y4YjdlTDdrWm5XNnUxYUkrWTZ5TXhBVmhBZTZnajloRzlncXlqOWcKRnRHNXZ5SzUrWVFqeVc3cDNNeldRMWxRSnFrdGErZThPcUxoTEovN3pxR2Q2WHhVMkg2K1o1a0NBd0VBQWFBQQpNQTBHQ1NxR1NJYjNEUUVCQ3dVQUE0SUJBUUJHN3ZNajRVNnYzWngwQ0l5dkJtTXE4d1ozempEMEZ2QzluOUovCndKRzZGM3JMV1ZicTJuSnZHOHo2T3o0bkh4NkxaUTJiRThwTTZtTm5YUm1XcTNwTTlVNEwxVVFKWXA2TjN1WlEKdy9odVc1VzArN2FXaHc2Q3ZFNXZBcWdXelVRSzhkWjBrTDFUN2RNMG1JMUcrM1YxU3hxRzdCN0t4SDZ5R2YzZwo3YzZsNmZGWkpmRzRRa0QyeXgyVDdyMGI4VHpJYmUybDl0My9oSlh4N0l2ejB2TmU0ZzlwM0hjek01S1Q1WWQwClJrMCtEbVVXSmZIcjBoMmhLMmFTZjdZKzlEanpWeDdCN2dYNUg5S1E0SDhsTXo5cjFvWjFOejR6akQ3TGc1aCsKTkJRTWJKOStHelRGN2hJcVoxUm0xUHpFR0RqUmRNbFE5bytaR3g4cFU2VU9RMHUzdUEyZkpyN0txM3g5WDZhQQotLS0tLUVORCBDRVJUSUZJQ0FURSBSRVFVRVNULS0tLS0=" +
                "  signerName: kubernetes.io/kube-apiserver-client\n" +
                "  usages:\n" +
                "    - client auth";

        return base64EncodedCertificate;
    }


    private static String encodeBase64(String content) {
        byte[] encodedBytes = Base64.getEncoder().encode(content.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}

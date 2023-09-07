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
    public void selfApproveCSR(String certificatePem,String privatePem) throws Exception{

        String csrContent = PEMImporter.convertCertToCSR(certificatePem, privatePem);
        String csrYaml = getCsrYaml(csrContent);

        CertificateSigningRequest csr = new CertificateSigningRequestBuilder()
                .withNewMetadata()
                .withName("mock111s")
                .endMetadata()
                .withNewSpec()
                .withRequest(csrYaml)
                .withSignerName("kubernetes.io/kubelet-serving")
                .withUsages(
                    "client auth"
                )
                .endSpec()
                .withApiVersion("v1")
                .build();

        client.certificates().v1().certificateSigningRequests().resource(csr).create();

//        CertificateSigningRequestCondition csrCondition = new CertificateSigningRequestConditionBuilder()
//                .withType("Approved")
//                .withStatus("True")
//                .withReason("ApprovedViaRESTApi")
//                .withMessage("Approved by REST API /approval endpoint.")
//                .build();
//        client.certificates().v1().certificateSigningRequests().withName("mock-agent-csr").approve(csrCondition);

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


    public static void main(String[] args) {
        String csrContent = "-----BEGIN CERTIFICATE REQUEST-----\n" +
                "MIIC2DCCAcACAQAwgY0xCzAJBgNVBAYTAkNOMRIwEAYDVQQIDAlHdWFuZ2Rvbmcx\n" +
                "EjAQBgNVBAcMCUd1YW5nIFN0cmF0ZTEPMA0GA1UECgwGRGV2ZWxvcG1lbnQxCzAJ\n" +
                "BgNVBAsMAklUMRswGQYDVQQDDBJkZXZlbG9wbWVudC5jb20gRXhhbXBsZTEhMB8G\n" +
                "CSqGSIb3DQEJARYSbWFpbEBleGFtcGxlLmNvbTCCASIwDQYJKoZIhvcNAQEBBQAD\n" +
                "ggEPADCCAQoCggEBAK0RyJg3u5sHbXj6j2QIj2EJ3C+H3+QH5q9PjF9ZP1L4WdaS\n" +
                "fj+3aCqS9WjTz0Oyf/6rWU6rJ4r5/8d6VvGgKoZQqJQ1lZ1OeJ0E1uY0oXGY3QVx\n" +
                "kW2F1oUZ5mYxL0KtXs2rS2LXQ5n3tZa0w3W1tL3S0bL6P6w3PmTQmJfEi5YtYl3x\n" +
                "Xa8Kx6X7q0r3TJ8tZ4dUoL8qz1+U9fZjvzYjJc2zHxGvF2U2w3Jn7e0dVK69yG6p\n" +
                "R/7eL9lWg2U8QfL8uQJ7mS8Ov8b7eL7kZnW6u1aI+Y6yMxAVhAe6gj9hG9gqyj9g\n" +
                "FtG5vyK5+YQjyW7p3MzWQ1lQJqkta+e8OqLhLJ/7zqGd6XxU2H6+Z5kCAwEAAaAA\n" +
                "MA0GCSqGSIb3DQEBCwUAA4IBAQBG7vMj4U6v3Zx0CIyvBmMq8wZ3zjD0FvC9n9J/\n" +
                "wJG6F3rLWVbq2nJvG8z6Oz4nHx6LZQ2bE8pM6mNnXRmWq3pM9U4L1UQJYp6N3uZQ\n" +
                "w/huW5W0+7aWhw6CvE5vAqgWzUQK8dZ0kL1T7dM0mI1G+3V1SxqG7B7KxH6yGf3g\n" +
                "7c6l6fFZJfG4QkD2yx2T7r0b8TzIbe2l9t3/hJXx7Ivz0vNe4g9p3HczM5KT5Yd0\n" +
                "Rk0+DmUWJfHr0h2hK2aSf7Y+9DjzVx7B7gX5H9KQ4H8lMz9r1oZ1Nz4zjD7Lg5h+\n" +
                "NBQMbJ9+GzTF7hIqZ1Rm1PzEGDjRdMlQ9o+ZGx8pU6UOQ0u3uA2fJr7Kq3x9X6aA\n" +
                "-----END CERTIFICATE REQUEST-----";

        String encodedCsr = encodeBase64(csrContent);
        System.out.println("Encoded CSR: " + encodedCsr);
    }

    private static String encodeBase64(String content) {
        byte[] encodedBytes = Base64.getEncoder().encode(content.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}

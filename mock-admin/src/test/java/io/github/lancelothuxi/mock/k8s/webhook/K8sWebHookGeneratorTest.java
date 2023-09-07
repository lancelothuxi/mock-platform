package io.github.lancelothuxi.mock.k8s.webhook;

import io.fabric8.kubernetes.api.model.certificates.v1.CertificateSigningRequest;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


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

    @Test
    public void testDeleteWebhook() {
        K8sWebHookGenerator k8sWebHookGenerator=new K8sWebHookGenerator();
        k8sWebHookGenerator.deleteWebhook();

    }

  @Test
  void csrTest() {

      try (KubernetesClient client = new KubernetesClientBuilder().build()) {
          CertificateSigningRequest csr = client.certificates().v1().certificateSigningRequests()
                  .load(K8sWebHookGeneratorTest.class.getResourceAsStream("/test-csr-v1.yml")).item();
          client.certificates().v1().certificateSigningRequests().resource(csr).create();
      }

  }

  @Test
  void deleteWebhook() {


  }

  @Test
  void selfApproveCSR() throws Exception {

      URL location = K8sWebHookGeneratorTest.class.getProtectionDomain().getCodeSource().getLocation();
      Path path = Paths.get(location.toURI());
      String basePath = path.toString();

      // PEM 文件路径
      String certificatePath = basePath+"/certificate.pem";
      String privateKeyPath = path+"/private-key.pem";


      K8sWebHookGenerator k8sWebHookGenerator=new K8sWebHookGenerator();
      k8sWebHookGenerator.selfApproveCSR(certificatePath,privateKeyPath,"mynam22");
  }
}
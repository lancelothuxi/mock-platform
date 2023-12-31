package io.github.lancelothuxi.mock.ssl;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/9/4 下午8:35
 */
class SslUtilsTest {

  @Test
  public void testConvertPemToJks() throws Exception {

    URL location = SslUtilsTest.class.getProtectionDomain().getCodeSource().getLocation();
    Path path = Paths.get(location.toURI());

    String basePath = path.toString();

    // PEM 文件路径
    String privateKeyPath = path + "/private-key.pem";
    String certificatePath = path + "/certificate.pem";
    // JKS 文件路径
    String jksPath = path + "/keystore.jks";
    // 密钥库密码
    String keyStorePassword = "your-password";
    // 密钥别名
    String keyAlias = "mycert";

    // 将 PEM 文件转换为 JKS 文件
    SslUtils.convertPemToJks(privateKeyPath, certificatePath, jksPath, keyStorePassword, keyAlias);

    // 验证 JKS 文件是否成功生成
    File jksFile = new File(jksPath);
    assertTrue(jksFile.exists());

    // 验证 JKS 文件中的私钥和证书是否正确
    try (FileInputStream jksInputStream = new FileInputStream(jksFile)) {
      KeyStore keyStore = KeyStore.getInstance("JKS");
      keyStore.load(jksInputStream, keyStorePassword.toCharArray());

      PrivateKey privateKey =
          (PrivateKey) keyStore.getKey(keyAlias, keyStorePassword.toCharArray());
      Certificate certificate = keyStore.getCertificate(keyAlias);

      assertTrue(privateKey != null);
      assertTrue(certificate != null);

      // 可以进行进一步的验证，例如验证私钥和证书的匹配性等
    }
  }

  @Test
  void convertPemToJks() {}
}

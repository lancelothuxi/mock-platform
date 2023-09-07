package io.github.lancelothuxi.mock.ssl;

import org.apache.shiro.util.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/9/5 上午10:08
 */
class PEMImporterTest {

  @Test
  void createSSLFactory() throws Exception {
    URL location = PEMImporterTest.class.getProtectionDomain().getCodeSource().getLocation();
    Path path = Paths.get(location.toURI());
    String basePath = path.toString();

    // PEM 文件路径
    String privateKeyPath = basePath+"/private-key.pem";
    String certificatePath = basePath+"/certificate.pem";
    PEMImporter.createSSLFactory(new File(privateKeyPath),new File(certificatePath),"");
  }

  @Test
  void createKeyStore() throws Exception{
    URL location = PEMImporterTest.class.getProtectionDomain().getCodeSource().getLocation();
    Path path = Paths.get(location.toURI());
    String basePath = path.toString();

    // PEM 文件路径
    String privateKeyPath = basePath+"/private-key.pem";
    String certificatePath = basePath+"/certificate.pem";
    KeyStore keyStore = PEMImporter.createKeyStore(new File(privateKeyPath), new File(certificatePath), "");
    Assert.notNull(keyStore);
  }

  @Test
  void convertCertToCSR() throws Exception{

    URL location = SslUtilsTest.class.getProtectionDomain().getCodeSource().getLocation();
    Path path = Paths.get(location.toURI());

    // PEM 文件路径
    String privateKeyPath = path+"/private-key.pem";
    String certificatePath = path+"/certificate.pem";

    String convertCertToCSR = PEMImporter.convertCertToCSR(certificatePath, privateKeyPath);

    System.out.println("convertCertToCSR = " + convertCertToCSR);
  }
}
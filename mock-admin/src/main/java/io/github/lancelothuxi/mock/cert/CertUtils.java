package io.github.lancelothuxi.mock.cert;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/8/30 下午10:55
 */
public class CertUtils {

    private static void convertPemToJks(String privateKeyPath, String certificatePath, String jksPath,
                                        String keyStorePassword, String keyAlias) throws Exception {
        // 读取 PEM 密钥文件
        try (FileReader privateKeyReader = new FileReader(privateKeyPath);
             FileReader certificateReader = new FileReader(certificatePath)) {
            PEMParser pemParser = new PEMParser(privateKeyReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

            // 解析 PEM 私钥文件
            PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
            PrivateKey privateKey = converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());

            // 解析 PEM 证书文件
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(certificateReader);

            // 创建 JKS 密钥库并存储私钥和证书
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, null);
            keyStore.setKeyEntry(keyAlias, privateKey, keyStorePassword.toCharArray(),
                    new Certificate[]{certificate});

            // 保存 JKS 密钥库到文件
            try (FileOutputStream outputStream = new FileOutputStream(jksPath)) {
                keyStore.store(outputStream, keyStorePassword.toCharArray());
            }
        }
    }

}

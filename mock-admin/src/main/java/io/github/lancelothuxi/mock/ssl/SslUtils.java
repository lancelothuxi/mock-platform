package io.github.lancelothuxi.mock.ssl;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/9/4 下午8:26
 */
public class SslUtils {

    public static void convertPemToJks(String privateKeyPath, String certificatePath, String jksPath,
                                        String keyStorePassword, String keyAlias) throws Exception {
        // 加载 PEM 文件中的私钥
        byte[] privateKeyBytes;
        try (FileInputStream privateKeyInputStream = new FileInputStream(privateKeyPath)) {
            privateKeyBytes = readAllBytes(privateKeyInputStream);
        }

        // 加载 PEM 文件中的证书
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate certificate;
        try (FileInputStream certificateInputStream = new FileInputStream(certificatePath)) {
            certificate = certificateFactory.generateCertificate(certificateInputStream);
        }

        // 创建 JKS 密钥库并存储私钥和证书
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setKeyEntry(keyAlias, toPrivateKey(privateKeyBytes), keyStorePassword.toCharArray(),
                new Certificate[]{certificate});

        // 保存 JKS 密钥库到文件
        try (FileOutputStream outputStream = new FileOutputStream(jksPath)) {
            keyStore.store(outputStream, keyStorePassword.toCharArray());
        }
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return outputStream.toByteArray();
    }

    private static PrivateKey toPrivateKey(byte[] privateKeyBytes) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(privateKeyBytes);
        keyFactory.generatePublic(publicKeySpec);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(privateKeySpec);
    }


}

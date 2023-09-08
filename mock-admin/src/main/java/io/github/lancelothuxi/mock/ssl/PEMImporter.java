package io.github.lancelothuxi.mock.ssl;

import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/9/5 上午10:05
 */
public class PEMImporter {

  public static SSLServerSocketFactory createSSLFactory(
      File privateKeyPem, File certificatePem, String password) throws Exception {
    final SSLContext context = SSLContext.getInstance("TLS");
    final KeyStore keystore = createKeyStore(privateKeyPem, certificatePem, password);
    final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(keystore, password.toCharArray());
    final KeyManager[] km = kmf.getKeyManagers();
    context.init(km, null, null);
    return context.getServerSocketFactory();
  }

  /**
   * Create a KeyStore from standard PEM files
   *
   * @param privateKeyPem the private key PEM file
   * @param certificatePem the certificate(s) PEM file
   * @param the password to set to protect the private key
   */
  public static KeyStore createKeyStore(
      File privateKeyPem, File certificatePem, final String password) throws Exception {
    final X509Certificate[] cert = createCertificates(certificatePem);
    final KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(null);
    // Import private key
    final PrivateKey key = createPrivateKey(privateKeyPem);
    keystore.setKeyEntry(privateKeyPem.getName(), key, password.toCharArray(), cert);
    return keystore;
  }

  /**
   * Create a KeyStore from standard PEM files
   *
   * @param privateKeyPem the private key PEM file
   * @param certificatePem the certificate(s) PEM file
   * @param the password to set to protect the private key
   */
  public static KeyStore createKeyStore(
          InputStream privateKeyPem,String alias, InputStream certificatePem, final String password) throws Exception {
    final X509Certificate[] cert = createCertificates(certificatePem);
    final KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(null);
    // Import private key
    final PrivateKey key = createPrivateKey(privateKeyPem);
    keystore.setKeyEntry(alias, key, password.toCharArray(), cert);
    return keystore;
  }

  private static PrivateKey createPrivateKey(InputStream privateKeyPem) throws Exception {
    final BufferedReader r = new BufferedReader(new InputStreamReader(privateKeyPem));
    String s = r.readLine();
    if (s == null || !s.contains("BEGIN PRIVATE KEY")) {
      r.close();
      throw new IllegalArgumentException("No PRIVATE KEY found");
    }
    final StringBuilder b = new StringBuilder();
    s = "";
    while (s != null) {
      if (s.contains("END PRIVATE KEY")) {
        break;
      }
      b.append(s);
      s = r.readLine();
    }
    r.close();
    final String hexString = b.toString();
    final byte[] bytes = DatatypeConverter.parseBase64Binary(hexString);
    return generatePrivateKeyFromDER(bytes);
  }


  private static PrivateKey createPrivateKey(File privateKeyPem) throws Exception {
    return createPrivateKey(new FileInputStream(privateKeyPem));
  }

  private static X509Certificate[] createCertificates(InputStream certificatePem) throws Exception {
    final List<X509Certificate> result = new ArrayList<X509Certificate>();
    final BufferedReader r = new BufferedReader(new InputStreamReader(certificatePem));
    String s = r.readLine();
    if (s == null || !s.contains("BEGIN CERTIFICATE")) {
      r.close();
      throw new IllegalArgumentException("No CERTIFICATE found");
    }
    StringBuilder b = new StringBuilder();
    while (s != null) {
      if (s.contains("END CERTIFICATE")) {
        String hexString = b.toString();
        final byte[] bytes = DatatypeConverter.parseBase64Binary(hexString);
        X509Certificate cert = generateCertificateFromDER(bytes);
        result.add(cert);
        b = new StringBuilder();
      } else {
        if (!s.startsWith("----")) {
          b.append(s);
        }
      }
      s = r.readLine();
    }
    r.close();

    return result.toArray(new X509Certificate[result.size()]);
  }

  private static X509Certificate[] createCertificates(File certificatePem) throws Exception {
    return createCertificates(new FileInputStream(certificatePem));
  }

  private static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes)
      throws InvalidKeySpecException, NoSuchAlgorithmException {
    final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    final KeyFactory factory = KeyFactory.getInstance("RSA");
    return (RSAPrivateKey) factory.generatePrivate(spec);
  }

  private static X509Certificate generateCertificateFromDER(byte[] certBytes)
      throws CertificateException {
    final CertificateFactory factory = CertificateFactory.getInstance("X.509");
    return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
  }
  /**
   * @param certFile
   * @param privateKeyFile
   * @throws Exception
   * @return
   */
  public static String convertCertToCSR(String certFile, String privateKeyFile) throws Exception {

    X509Certificate[] certificates = createCertificates(new File(certFile));

    assert certificates != null;
    assert certificates.length == 1;

    X509Certificate certificate = certificates[0];

    // 读取私钥文件
    PrivateKey privateKey = createPrivateKey(new File(privateKeyFile));

    // 获取证书主题信息
    X500Principal subject = certificate.getSubjectX500Principal();

    // 创建CSR请求
    PKCS10CertificationRequestBuilder csrBuilder =
        new JcaPKCS10CertificationRequestBuilder(subject, certificate.getPublicKey());
    ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
    PKCS10CertificationRequest csr = csrBuilder.build(signer);

    // 将CSR请求写入文件
    StringWriter writer = new StringWriter();
    writer.write("-----BEGIN CERTIFICATE REQUEST-----\n");
    writer.write(Base64.getEncoder().encodeToString(csr.getEncoded()));
    writer.write("\n-----END CERTIFICATE REQUEST-----");
    writer.close();

    return writer.toString();
  }
}

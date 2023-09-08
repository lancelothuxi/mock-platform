package io.github.lancelothuxi.mock.ssl;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * @author lancelot
 * @version 1.0
 * @date 2023/9/4 下午7:36
 */
@Configuration
public class SslConfiguration {

  private final ResourceLoader resourceLoader;

  public SslConfiguration(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Bean
  public SSLContext sslContext() throws Exception {
    InputStream certInputStream = resourceLoader.getResource("classpath:" + "cert/certificate.pem").getInputStream();
    InputStream privateKeyInputStream = resourceLoader.getResource("classpath:" + "cert/private-key.pem").getInputStream();
    String password="";
    KeyStore keyStore = PEMImporter.createKeyStore(privateKeyInputStream, "", certInputStream, "");
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, password.toCharArray());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
    return sslContext;
  }

  @Bean
  public ServletWebServerFactory servletContainer() {
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
    tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
    return tomcat;

  }

  private Connector createHTTPConnector() {
    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
    //同时启用http（8080）、https（8443）两个端口
    connector.setScheme("http");
    connector.setSecure(false);
    connector.setPort(8555);
    connector.setRedirectPort(8443);
    return connector;
  }
}

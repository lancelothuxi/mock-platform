package io.github.lancelothuxi.mock.ssl;

import org.springframework.context.annotation.Configuration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
/**
 * @author lancelot
 * @version 1.0
 * @date 2023/9/4 下午7:36
 */
@Configuration
public class SslConfiguration {

//    @Bean
//    public SslContextFactory.Server sslContextFactory() {
//        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
//        sslContextFactory.setSslContext(sslContext);
//        sslContextFactory.setIncludeProtocols(protocols);
//        sslContextFactory.setIncludeCipherSuites(ciphers);
//        sslContextFactory.setNeedClientAuth(true);
//        return sslContextFactory;
//    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(SslContextFactory.Server sslContextFactory) {
        JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
        factory.setPort(8443);
        JettyServerCustomizer jettyServerCustomizer = server -> server.setConnectors(new Connector[]{new ServerConnector(server, sslContextFactory)});
        factory.setServerCustomizers(Collections.singletonList(jettyServerCustomizer));
        return factory;
    }
}

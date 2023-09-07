package io.github.lancelothuxi.mock.framework.config;

import io.github.lancelothuxi.mock.common.utils.StringUtils;
import io.github.lancelothuxi.mock.common.xss.XssFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter配置
 *
 * @author lancelot huxisuz@gmail.com
 */
@Configuration
@ConditionalOnProperty(value = "xss.enabled", havingValue = "true")
public class FilterConfig {
  @Value("${xss.excludes}")
  private String excludes;

  @Value("${xss.urlPatterns}")
  private String urlPatterns;

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Bean
  public FilterRegistrationBean xssFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setDispatcherTypes(DispatcherType.REQUEST);
    registration.setFilter(new XssFilter());
    registration.addUrlPatterns(StringUtils.split(urlPatterns, ","));
    registration.setName("xssFilter");
    registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
    Map<String, String> initParameters = new HashMap<String, String>();
    initParameters.put("excludes", excludes);
    registration.setInitParameters(initParameters);
    return registration;
  }
}

package io.github.lancelothuxi.mock.framework.datasource;

import io.github.lancelothuxi.mock.common.config.datasource.DynamicDataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源
 *
 * @author lancelot huxisuz@gmail.com
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
  public DynamicDataSource(
      DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
    super.setDefaultTargetDataSource(defaultTargetDataSource);
    super.setTargetDataSources(targetDataSources);
    super.afterPropertiesSet();
  }

  @Override
  protected Object determineCurrentLookupKey() {
    return DynamicDataSourceContextHolder.getDataSourceType();
  }
}

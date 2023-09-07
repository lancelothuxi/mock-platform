package io.github.lancelothuxi.mock.functions.impl;

import com.alibaba.fastjson.JSONPath;
import io.github.lancelothuxi.mock.functions.AbstractFunction;
import io.github.lancelothuxi.mock.functions.CompoundVariable;

import java.util.Collection;

public class ReferFunction extends AbstractFunction {

  private static final String KEY = "__refer";
  private static final int CHARS = 2;
  private static final int PARAM_NAME = 3;
  private CompoundVariable[] values;

  public ReferFunction() {
    super();
  }

  @Override
  public String execute(Object... args) {
    if (args == null || args.length == 0) {
      return "";
    }

    String requestData = (String) args[0];
    final String referJsonPathExpression = values[0].execute();

    try {
      final Object jsonPathValue = JSONPath.read(requestData, referJsonPathExpression);
      if (jsonPathValue == null) {
        return "";
      }

      return jsonPathValue.toString();
    } catch (Exception ex) {
      return "";
    }
  }

  @Override
  public void setParameters(Collection<CompoundVariable> parameters) {
    // TODO 参数个数校验
    values = parameters.toArray(new CompoundVariable[parameters.size()]);
  }

  @Override
  public String getReferenceKey() {
    return KEY;
  }
}

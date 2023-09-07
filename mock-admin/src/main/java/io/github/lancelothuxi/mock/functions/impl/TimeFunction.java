package io.github.lancelothuxi.mock.functions.impl;

import io.github.lancelothuxi.mock.functions.AbstractFunction;
import io.github.lancelothuxi.mock.functions.CompoundVariable;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.regex.Pattern;

public class TimeFunction extends AbstractFunction {

  private static final String KEY = "__time";

  private static final Pattern DIVISOR_PATTERN = Pattern.compile("/\\d+");

  private String format = "";

  public TimeFunction() {
    super();
  }

  @Override
  public String execute(Object... args) {
    String datetime = "";
    if (format.isEmpty()) {
      datetime = Long.toString(System.currentTimeMillis());
    }
    if (DIVISOR_PATTERN.matcher(format).matches()) { // divisor is a positive number
      long div = Long.parseLong(format.substring(1)); // should never case NFE
      datetime = Long.toString(System.currentTimeMillis() / div);
    } else {
      DateTimeFormatter df =
          DateTimeFormatter // Not synchronised, so can't be shared
              .ofPattern(format)
              .withZone(ZoneId.systemDefault());
      datetime = df.format(Instant.now());
    }
    return datetime;
  }

  @Override
  public void setParameters(Collection<CompoundVariable> parameters) {
    // TODO 参数个数校验

    Object[] values = parameters.toArray();
    int count = values.length;

    if (count > 0) {
      format = ((CompoundVariable) values[0]).execute();
    }
  }

  @Override
  public String getReferenceKey() {
    return KEY;
  }
}

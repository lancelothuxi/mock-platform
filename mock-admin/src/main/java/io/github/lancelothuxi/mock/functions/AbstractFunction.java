package io.github.lancelothuxi.mock.functions;

import java.util.Collection;

public abstract class AbstractFunction implements Function {

  @Override
  public abstract String execute(Object... args) throws Exception;

  @Override
  public abstract void setParameters(Collection<CompoundVariable> parameters) throws Exception;

  @Override
  public abstract String getReferenceKey();
}

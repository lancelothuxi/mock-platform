package io.github.lancelothuxi.mock.functions;

import java.util.Collection;

public interface Function {

  String getReferenceKey();

  void setParameters(Collection<CompoundVariable> parameters) throws Exception;

  String execute(Object... args) throws Exception;
}

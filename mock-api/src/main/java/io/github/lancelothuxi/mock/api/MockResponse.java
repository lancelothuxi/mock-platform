package io.github.lancelothuxi.mock.api;

import java.io.Serializable;

/**
 * @author lancelot
 * @version 1.0
 * @since 2023/8/14 下午1:49
 */
public class MockResponse implements Serializable {

  /** mock data* */
  private String data;

  /** resp code* */
  private int code;

  /** error message* */
  private String errorMessage;

  public MockResponse() {}

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getData() {
    return this.data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public int getCode() {
    return this.code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public boolean success() {
    return this.code == 0;
  }
}

package io.github.lancelothuxi.mock.mock;


/**
 * @author lancelot
 * @version 1.0
 * @date 2023/2/22 上午11:03
 */
public class DubboRestResp<T> {

    private String jsonrpc;

    private String id;

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

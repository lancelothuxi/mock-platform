package io.github.lancelothuxi.mock.examples;



public class Example {
    public static void main(String[] args) throws Exception{

        // 添加 Mock Agent 参数
        // 创建被 mock 的类的实例
        ExampleService service = new ExampleService();
        // 调用被 mock 的方法
        int result = service.add(1, 2);

        System.out.println("result = " + result);

    }
}

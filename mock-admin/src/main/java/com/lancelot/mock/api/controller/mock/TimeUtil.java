package com.lancelot.mock.api.controller.mock;

import com.lancelot.mock.api.controller.tool.NativeLoader;


public class TimeUtil {

    static {
        //根据操作系统判断，如果是linux系统则加载c++方法库
        String systemType = System.getProperty("os.name");
        String ext = (systemType.toLowerCase().indexOf("win") != -1) ? ".dll" : ".so";
        if(ext.equals(".so")) {
            try {
                NativeLoader.loader( "native" );
            } catch (Exception e) {
                System.out.println("加载so库失败");
            }
        }
    }

    public native void delay(int ms);
}
package com.agileboot.common.enums.common;

import com.agileboot.common.enums.BasicEnum;

/**
 * Http Method
 * @author valarchie
 */
public enum RequestMethodEnum implements BasicEnum<Integer> {

    /**
     * 菜单类型
     */
    GET(1, "GET"),
    POST(2, "POST"),
    PUT(3, "PUT"),
    DELETE(4, "DELETE"),
    UNKNOWN(-1, "UNKNOWN");

    private final int value;
    private final String description;

    RequestMethodEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }


}

package com.xiaozhanxiang.simplegridview.bean;

import java.io.Serializable;

/**
 * author: dai
 * date:2019/6/20
 */
public class TestSerializable  implements Serializable{
    private static final long serialVersionUID = 1L;

    public String name;
    public int age;

    public transient String des;  // 标记为 transient 的字段不会进行序列化和反序列化
    public static String id; // 静态变量也不参与序列化


    public TestSerializable(String name, int age, String des) {
        this.name = name;
        this.age = age;
        this.des = des;
    }


    @Override
    public String toString() {
        return "TestSerializable{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", des='" + des + '\'' +
                '}';
    }
}

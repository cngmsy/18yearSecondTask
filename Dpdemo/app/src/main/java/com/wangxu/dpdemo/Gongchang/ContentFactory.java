package com.wangxu.dpdemo.Gongchang;

/**
 * Created by dell on 2018/1/22.
 */

public class ContentFactory extends Factory{

    @Override
    public Product Concreteproduct() {
        return new ConcreteProductA();
    }
}

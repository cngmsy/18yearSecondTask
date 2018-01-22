package com.wangxu.dpdemo.Gongchang;

/**
 * Created by dell on 2018/1/22.
 */

public class Client {


    public  static  void main(String [] args){
        Factory factory= new ContentFactory();
        Product productp = factory.Concreteproduct();

        productp.method();

    }

}

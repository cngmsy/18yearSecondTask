package com.wangxu.dpdemo.Daili;

/**
 * Created by dell on 2018/1/22.
 */

public class client  {

    public  static  void main(String []  args){
        //构造真实主题对象
        Realsubject realsubject=new Realsubject();

        //通过真是主题对象构造一个代理对象
        ProxySubject proxySubject=new ProxySubject(realsubject);

        proxySubject.visit();

    }
}

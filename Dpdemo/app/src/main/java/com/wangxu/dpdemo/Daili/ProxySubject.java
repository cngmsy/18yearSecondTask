package com.wangxu.dpdemo.Daili;

/**
 * Created by dell on 2018/1/22.
 */

public class ProxySubject extends Subject{
    private Realsubject msubject;

    public ProxySubject(Realsubject msubject) {
        this.msubject = msubject;
    }

    @Override
    public void visit() {
        //通过真是主题调用的真实对象的逻辑方法
        msubject.visit();
    }
}

package com.wangxu.dpdemo.Danli;

/**
 * Created by dell on 2018/1/17.
 */

public class CEO extends Staff {
    private static final CEO mceo=new CEO();

    //构造函数私有化
    private CEO() {

    }

    public static Staff getmCEO(){
        return  mceo;
    }

    @Override
    public void work() {
        super.work();
        //管理vp
    }
}

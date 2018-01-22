package com.wangxu.dpdemo.Danli;

/**
 * Created by dell on 2018/1/17.
 */

//单利模式是用来让整个类 只拥有一个实例对象
//确保一个类只有一个实力对象
//而且自行实例化  并向整个系统提供这个实例

//1. 构造函数私有化
//2。 通过静态方法或者枚举返回单例类对象
//3.  确保单例类的对象  有且只有一个
//4.  确保单例类对象在反序列化是不会重新构建对象
public class Demo {

    public static void main(String[] args){
            Text text=new Text();
            Staff ceo = CEO.getmCEO();
            Staff ceo1 = CEO.getmCEO();
            text.addlist(ceo);
            text.addlist(ceo1);

            Staff vp1= new VP();
            Staff vp2 = new VP();
            Staff staff1 = new Staff();
            Staff staff2 = new Staff();
            Staff staff3 = new Staff();

            text.addlist(vp1);
            text.addlist(vp2);
            text.addlist(staff1);
            text.addlist(staff2);
            text.addlist(staff3);
            text.showAlllist();

    }
                //单利模式

//        Objcom.wangxu.dpdemo.CEO@232204a1
//        Objcom.wangxu.dpdemo.CEO@232204a1
//        Objcom.wangxu.dpdemo.VP@4aa298b7
//        Objcom.wangxu.dpdemo.VP@7d4991ad
//        Objcom.wangxu.dpdemo.Staff@28d93b30
//        Objcom.wangxu.dpdemo.Staff@1b6d3586
//        Objcom.wangxu.dpdemo.Staff@4554617c
}



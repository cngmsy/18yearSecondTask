package com.wangxu.dpdemo.Danli;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/1/17.
 */

public class Text {
    private List<Staff> list=new ArrayList<>();

    public  void addlist(Staff staff){
        list.add(staff);

    }

    public  void showAlllist(){
        for (Staff staff:list){
            System.out.print("Obj"+staff.toString());
        }

    }
}

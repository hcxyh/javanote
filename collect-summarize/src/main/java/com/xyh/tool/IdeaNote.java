package com.xyh.tool;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * idea学习笔记
 * @author xyh
 *
 */
public class IdeaNote {

    /**
     *
     alt+7 查看当前类的 struct  --> 比较相像于  outline
     alt +1 查看当前project 的机构

     ctrl + o 查看当前类的可override方法
     alt+ up/down 上下切换方法

     shift + alt + up/down  上下移动当前行


     ctrl + n  类似于eclipse 的ctrl+shift + r
     ctrl +alt + n


     ctrl+x 删除行
     ctrl+d 复制行
     clt+insert  settter/getter/构造器

     ctrl+enter  导包
     Ctrl+Shift+Space，自动补全代码
     Ctrl+/或Ctrl+Shift+/，注释（//或者
     Ctrl+Alt+L，格式化代码
     Ctrl+F，查找
     Ctrl+R，替换

     Ctrl+G，定位行

     Alt + Enter  IntelliJ IDEA 根据光标所在问题，提供快速修复选择，光标放在的位置不同提示的结果也不同 （必备）

     Alt + Insert 代码自动生成，如生成对象的 set / get 方法，构造函数，toString() 等 （必备）

     idea 的setting:
     https://www.cnblogs.com/jajian/p/8080612.html

     */


    public static void main(String[] args){

        List list = new ArrayList();
        System.out.print("1");
        for (int i=1 ; i <10 ; i++){
            int j = i;
            new Thread(() -> System.out.println("123")).start();
        }

        System.out.println("debugger is idea");

//        test();
    }



    public  static  void test(){
        System.out.print("测试方法");
    }



}

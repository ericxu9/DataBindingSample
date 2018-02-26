package com.xuyj.databinding.sample;
/*
 * 包名       com.xuyj.databinding.sample
 * 文件名:    User
 * 创建者:    xuyj
 * 创建时间:  2018/2/23 on 11:36
 * 描述:     TODO
 */

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class User extends BaseObservable
{
    private String name;
    private int    age;

    public User(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    @Bindable
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
//        notifyPropertyChanged(BR.age);
    }

    @Override
    public String toString()
    {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

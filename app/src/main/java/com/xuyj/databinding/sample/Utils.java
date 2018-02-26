package com.xuyj.databinding.sample;
/*
 * 包名       com.xuyj.databinding.sample
 * 文件名:    Utils
 * 创建者:    xuyj
 * 创建时间:  2018/2/24 on 10:17
 * 描述:     TODO
 */

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils
{
    //    @BindingAdapter({"bind:imageUrl"})
    //    官网文档是上面这样写的，但是编译的时候会有个警告，所以按下面这样写吧...
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url)
    {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @BindingConversion
    public static String converterDate(Date date)
    {
        return new SimpleDateFormat("yyyy-HH-mm hh:MM:ss").format(date);
    }


}

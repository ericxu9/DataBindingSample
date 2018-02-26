package com.xuyj.databinding.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CustomSetterActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        CustomSetterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_setter);
        binding.setImageUrl("http://www.liyongqiang.com/wp-content/uploads/2017/05/12-300x300.jpg");
    }
}

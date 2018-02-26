package com.xuyj.databinding.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

public class ConvertersActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ConvertersBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_converters);
        binding.setDate(new Date());
    }
}

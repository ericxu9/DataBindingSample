package com.xuyj.databinding.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FragmentUsageActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_usage);
//        getSupportFragmentManager().beginTransaction().add(getSupportFragmentManager().findFragmentById(R.id.fragment), null).commit();
    }
}

package com.xuyj.databinding.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xuyj.databinding.sample.databinding.ActivityBasicUsageBinding;

public class BasicUsageActivity extends AppCompatActivity
{

    private static final String TAG = "BasicUsageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_usage);
        ActivityBasicUsageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_basic_usage);
        binding.setUser(new User("许渺", 18));
        binding.setPresenter(new Presenter());
    }


    /**
     * 监听view事件
     */
    public class Presenter
    {
        public void buttonClick(View view, User user)
        {
            Toast.makeText(BasicUsageActivity.this, view.toString() + "-" + user.toString(), Toast.LENGTH_SHORT).show();
        }

        public void completeChanged(boolean isChecked)
        {
            Toast.makeText(BasicUsageActivity.this, "" + isChecked, Toast.LENGTH_SHORT).show();
        }
    }
}

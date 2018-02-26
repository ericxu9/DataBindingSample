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
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final ActivityBasicUsageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_basic_usage);
        binding.user.setText("zz");
        mUser = new User("许渺", 18);
        binding.setUser(mUser);
        binding.setPresenter(new Presenter());
    }

    /**
     * 监听view事件
     */
    public class Presenter
    {

        public void onClick(View view)
        {
            Toast.makeText(BasicUsageActivity.this, "method references", Toast.LENGTH_SHORT).show();
        }

        public void buttonClick(View view, User user)
        {
            Toast.makeText(BasicUsageActivity.this, view.toString() + "-" + user.toString(), Toast.LENGTH_SHORT).show();
            //Observable修改数据改变UI
            mUser.setName("许渺z");
            mUser.setAge(20);
        }

        public void completeChanged(boolean isChecked)
        {
            Toast.makeText(BasicUsageActivity.this, "" + isChecked, Toast.LENGTH_SHORT).show();
        }
    }
}

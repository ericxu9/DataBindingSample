package com.xuyj.databinding.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;

import com.xuyj.databinding.sample.databinding.ViewStubBinding;

public class ViewStubActivity extends AppCompatActivity
{

    private ViewStubBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_stub);
    }

    public void load(View view)
    {
        mBinding.viewStub.setOnInflateListener(new ViewStub.OnInflateListener()
        {
            @Override
            public void onInflate(ViewStub stub, View inflated)
            {
                mBinding.setName("啦啦啦");
            }
        });
        mBinding.viewStub.getViewStub().inflate();
    }
}

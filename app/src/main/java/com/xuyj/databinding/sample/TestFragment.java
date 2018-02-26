package com.xuyj.databinding.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuyj.databinding.sample.databinding.FragmentBinding;


public class TestFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        binding.setUser(new User("许渺z", 20));
        return binding.getRoot();
    }

}

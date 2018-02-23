package com.xuyj.databinding.sample;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuyj.databinding.sample.databinding.ItemListBinding;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ListAdapter(this));
    }


    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {

        private       List<User>     mListData;
        private final LayoutInflater mInflater;

        public ListAdapter(Context context)
        {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mListData = new ArrayList<>();
            for (int i = 0; i < 10; i++)
            {
                mListData.add(new User("许渺" + i, i + 10));
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            ItemListBinding binding = DataBindingUtil.inflate(mInflater, R.layout.item_list, parent, false);
            ViewHolder holder = new ViewHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, int position)
        {
            User user = mListData.get(position);
            holder.binding.setVariable(BR.user, user);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount()
        {
            return mListData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            ViewDataBinding binding;

            public ViewHolder(View itemView)
            {
                super(itemView);
            }

            public ViewDataBinding getBinding()
            {
                return binding;
            }

            public void setBinding(ViewDataBinding binding)
            {
                this.binding = binding;
            }
        }
    }

}

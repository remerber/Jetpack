package com.remember.jetpackstudy.ui.test;

import android.annotation.SuppressLint;
import android.app.AppComponentFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.remember.jetpackstudy.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.paging.PositionalDataSource;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.os.Build.VERSION_CODES.M;

public class TestPagingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_paging);
        recyclerView=findViewById(R.id.recyclerview);
        MyAdapter adapter=new MyAdapter();
        PagedList.Config config=new PagedList.Config.Builder()
                .setPageSize(10)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .build();
        LiveData<PagedList<DataBean>> listLiveData=new LivePagedListBuilder(new MyDataSourceFactory(),config)
                .build();
        listLiveData.observe(this, new Observer<PagedList<DataBean>>() {
            @Override
            public void onChanged(PagedList<DataBean> dataBeans) {
                adapter.submitList(dataBeans);
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

    }
    private class  MyDataSourceFactory  extends DataSource.Factory<Integer,DataBean>{

        @NonNull
        @Override
        public DataSource<Integer, DataBean> create() {
            return new MyDataSource();
        }
    }
    private class  MyDataSource extends PositionalDataSource<DataBean>{

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<DataBean> callback) {
            callback.onResult(loadData(0,10),0,10);
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<DataBean> callback) {
          callback.onResult(loadData(params.startPosition,10));
        }
    }
    private List<DataBean> loadData(int startPosition,int count) {
        List<DataBean> list = new ArrayList();

        for (int i = 0; i < count; i++) {
            DataBean data = new DataBean();
            data.id = startPosition + i;
            data.content = "测试的内容=" + data.id;
            list.add(data);
        }

        return list;
    }
    private class  DataBean{
        public  int id;
        public  String content;
    }
    private class  MyAdapter extends PagedListAdapter<DataBean,  MyViewHolder>{


        protected MyAdapter() {
            super(mDiffCallback);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getApplicationContext()).inflate(android.R.layout.simple_expandable_list_item_2,null);
            MyViewHolder viewHolder=new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            DataBean dataBean=getItem(position);
            holder.text1.setText(String.valueOf(position));
            holder.text2.setText(String.valueOf(dataBean.content));

        }
    }
    private DiffUtil.ItemCallback<DataBean> mDiffCallback=new DiffUtil.ItemCallback<DataBean>() {
        @Override
        public boolean areItemsTheSame(@NonNull DataBean oldItem, @NonNull DataBean newItem) {
            Log.d("DiffCallback","areItemsTheSame");
            return oldItem.id==newItem.id;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull DataBean oldItem, @NonNull DataBean newItem) {
            Log.d("DiffCallback","areContentsTheSame");
            return (oldItem == newItem);
        }
    };
    private  class  MyViewHolder extends RecyclerView.ViewHolder{
        public TextView text1;
        public TextView text2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //系统ui
            text1 = itemView.findViewById(android.R.id.text1);
            text1.setTextColor(Color.RED);

            text2 = itemView.findViewById(android.R.id.text2);
            text2.setTextColor(Color.BLUE);
        }

    }
}

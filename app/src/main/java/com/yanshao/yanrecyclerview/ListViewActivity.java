package com.yanshao.yanrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yanshao.yanrecyclerview.widget.YanRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListViewActivity extends AppCompatActivity {

    @Bind(R.id.yanview)
    YanRecyclerView yanview;
    @Bind(R.id.activity_list_view)
    RelativeLayout activityListView;
RecyclerViewAdapter recyclerViewAdapter;
    List<String> list = new ArrayList<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ButterKnife.bind(this);


        for (int i = 'A'; i <= 'z'; i++) {
            list.add("" + (char) i);
        }
        yanview.setIsopenLoad(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewAdapter = new RecyclerViewAdapter(ListViewActivity.this, list);
        yanview.setAdapter(recyclerViewAdapter);
        yanview.setLayoutManager(linearLayoutManager);
        yanview.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Toast.makeText(ListViewActivity.this, "ItemClick=" + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnItemLongClick(View v, int position) {
                Toast.makeText(ListViewActivity.this, "ItemLong=" + position, Toast.LENGTH_LONG).show();

            }
        });

        yanview.setonRefreshListener(new YanRecyclerView.onRefreshListener() {
            @Override
            public void refresh() {
                new Handler() {


                    @Override
                    public void handleMessage(Message msg) {
                        recyclerViewAdapter.AddData(0);
                        yanview.setOnRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }

            @Override
            public void loadingMore() {
                new Handler() {


                    @Override
                    public void handleMessage(Message msg) {
                        recyclerViewAdapter.aAddData(recyclerViewAdapter.getItemCount());
                        yanview.setOnRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }
        });
    }
}

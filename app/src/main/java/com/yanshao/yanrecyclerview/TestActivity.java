package com.yanshao.yanrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yanshao.yanrecyclerview.widget.YanRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.yanview)
    YanRecyclerView yanview;
    @Bind(R.id.activity_test)
    RelativeLayout activityTest;
    TestAdapter recyclerViewAdapter;
    List<String> list = new ArrayList<>();
    @Bind(R.id.open)
    CheckBox open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        for (int i = 'A'; i <= 'z'; i++) {
            list.add("" + (char) i);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewAdapter = new TestAdapter(this, list);
        yanview.setAdapter(recyclerViewAdapter);
        yanview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        yanview.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Toast.makeText(TestActivity.this, "ItemClick=" + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnItemLongClick(View v, int position) {
                Toast.makeText(TestActivity.this, "ItemLong=" + position, Toast.LENGTH_LONG).show();
               // recyclerViewAdapter.DeleteData(position);
            }
        });
        yanview.setIsopenLoad(true);
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

        open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    yanview.setIsopenLoad(true);
                }else{
                    yanview.setIsopenLoad(false);
                }
            }
        });
    }
}

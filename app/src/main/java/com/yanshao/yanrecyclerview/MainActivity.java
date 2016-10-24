package com.yanshao.yanrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.listview)
    Button listview;
    @Bind(R.id.gridview)
    Button gridview;
    @Bind(R.id.StaggeredGrid)
    Button StaggeredGrid;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.listview, R.id.gridview, R.id.StaggeredGrid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listview:
                startActivity(new Intent(MainActivity.this, ListViewActivity.class));
                break;
            case R.id.gridview:
                startActivity(new Intent(MainActivity.this, GridViewActivity.class));
                break;
            case R.id.StaggeredGrid:
                startActivity(new Intent(MainActivity.this, TestActivity.class));
                break;
        }
    }
}

package com.jci.teacherclasslock.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jci.teacherclasslock.R;
import com.jci.teacherclasslock.adapter.ClassListAdapter;
import com.jci.teacherclasslock.adapter.ClassListDividerItemDecoration;
import com.jci.teacherclasslock.core.ConstantValues;

public class ClassmateListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private ClassListAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmate_list);
        String s = getIntent().getStringExtra("position");
        initData(s);
        initWidget();

    }

    private void initData(String s) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ClassListAdapter(ConstantValues.map.get(s));

    }
    private void initWidget() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mClassmateActivity_List);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new ClassListDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }
}

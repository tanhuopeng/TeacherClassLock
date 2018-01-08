package com.jci.teacherclasslock.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jci.teacherclasslock.R;
import com.jci.teacherclasslock.adapter.ClassListAdapter;
import com.jci.teacherclasslock.adapter.ClassListDividerItemDecoration;
import com.jci.teacherclasslock.core.ConstantValues;
import com.jci.teacherclasslock.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.UserInfo;


public class ClassListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private ClassListAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        initData();
        initWidget();
        loadData();
    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ClassListAdapter(ConstantValues.groupIDList);
        mAdapter.setOnItemClickListener(new ClassListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ClassListActivity.this,ClassmateListActivity.class);
                intent.putExtra("position",ConstantValues.groupIDList.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                ConstantValues.classes = ConstantValues.groupIDList.get(position).substring(0, 4);
                showCustomDialog();
            }
        });


    }


    private void initWidget() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mClassListActivity_List);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new ClassListDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void showCustomDialog() {
        final CustomDialog customDialog = new CustomDialog(ClassListActivity.this, R.style.CustomDialog);//设置自定义背景
        customDialog.setState("0");
        customDialog.setTitle("编辑课程");
        customDialog.setMsg("是否编辑课程？");
        customDialog.setOnNegateListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.setOnPositiveListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ClassListActivity.this, SelectTermActivity.class);
                startActivity(intent);
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    private void loadData(){
        for (int j = 0; j < ConstantValues.idList.size(); j++){
            final int finalJ = j;
            JMessageClient.getGroupMembers(ConstantValues.idList.get(j),
                    new GetGroupMembersCallback() {
                        @Override
                        public void gotResult(int i, String s, List<UserInfo> list) {
                            if (i == 0){
                                ArrayList<String> l = new ArrayList<String>();
                                for ( UserInfo u:
                                     list) {
                                    l.add(u.getUserName());
                                }
                                ConstantValues.map.put(ConstantValues.groupIDList.get(finalJ),l);
                            }
                        }
                    });
        }
    };

}

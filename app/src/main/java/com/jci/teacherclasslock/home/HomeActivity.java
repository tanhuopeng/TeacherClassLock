package com.jci.teacherclasslock.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.jci.teacherclasslock.R;
import com.jci.teacherclasslock.core.ConstantValues;
import com.jci.teacherclasslock.core.GlobalApplication;
import com.jci.teacherclasslock.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

public class HomeActivity extends AppCompatActivity {

    private Button mBtn_CreateClass;
    private Button mBtn_MyClass;
    private LayoutInflater inflater;
    private EditText mEdt_InputClass;
    //广播接收者
    private ChatBroadcastReceiver receiver;

    public static String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "17fa46d3a72e3bcad648403999064c8f");
        mBtn_CreateClass = (Button) this.findViewById(R.id.mhomeactivity_btn_create_class);
        mBtn_MyClass = (Button) this.findViewById(R.id.mhomeactivity_btn_my_class);
        initData();
        loadData();
        JMessageClient.registerEventReceiver(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        receiver = new ChatBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.UNLOCK);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    private void initData() {
        mBtn_MyClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/11/9 0009 一些一些事情
                Intent intent = new Intent(HomeActivity.this, ClassListActivity.class);
                startActivity(intent);

            }
        });
        mBtn_CreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClass();
            }
        });
    }

    private void createClass() {
        View dialog;
        inflater = LayoutInflater.from(this);
        dialog = inflater.inflate(R.layout.dialog_create_class,null);
        mEdt_InputClass = (EditText) dialog.findViewById(R.id.mEdt_InputClass);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("创建班级");
        builder.setView(dialog);
        builder.setCancelable(true);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = mEdt_InputClass.getText().toString().trim();
                if (!TextUtils.isEmpty(input)){
                    JMessageClient.createGroup(input, "群组", new CreateGroupCallback() {
                        @Override
                        public void gotResult(int i, String s, long l) {
                            if (i == 0){
                                ToastUtils.showShort("创建成功");
                                loadData();
                            }
                        }
                    });
                }else {
                    ToastUtils.showShort("班级不能为空");
                }
            }
        });
        builder.show();
    }

    private void loadData() {
        ConstantValues.groupIDList.clear();
        JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
            @Override
            public void gotResult(int i, String s, List<Long> list) {
                if (i == 0) {
                    for (Long id : list
                            ) {
                        JMessageClient.getGroupInfo(id, new GetGroupInfoCallback() {
                            @Override
                            public void gotResult(int i, String s, GroupInfo groupInfo) {
                                if (i == 0) {
                                    ConstantValues.groupIDList.add(groupInfo.getGroupName()+" "+groupInfo.getGroupID());
                                    ConstantValues.idList.add(groupInfo.getGroupID());
                                    ConstantValues.hashMap.put(groupInfo.getGroupName(),groupInfo.getGroupID());
                                } else {

                                }
                            }
                        });
                    }
                } else {
                }

            }
        });

    }

    public void onEvent(MessageEvent event){
        Message msg = event.getMessage();

        switch (msg.getContentType()){
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                if (textContent.getText().toString().length() >2 ){
                    dealAdd(msg.getFromUser().getUserName(),textContent.getText().toString());
                }else {
                    if (textContent.getText().toString().equals("2")){
                        ToastUtils.showLong(msg.getFromUser().getUserName()+"紧急解锁");
                    }else {
                        name = msg.getFromUser().getUserName();
                        GlobalApplication.getInstance().sendBroadcast(new Intent(ConstantValues.UNLOCK));
                    }
                }
                break;
        }
    }

    private void dealAdd(final String msg, final String textContent) {
        List<String> list = new ArrayList<String>();
        list.add(msg);
        JMessageClient.addGroupMembers(Long.parseLong(textContent), "748b4c279d7d89648adefba1", list, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0){
                    ToastUtils.showLong(msg+"加入"+textContent);
                }else {
                    ToastUtils.showShort(msg+"加入班级失败");
                }
            }
        });
    }

    class ChatBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConstantValues.UNLOCK)) {
                showAlertDialog(context);
            }
        }

    }

    public void showAlertDialog(Context context){
        // 获得广播发送的数据
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("确认解锁");
        dialogBuilder.setMessage("是否同意"+name+"请求解锁？");
        dialogBuilder.setCancelable(true);
        dialogBuilder.setNegativeButton("取消", null);
        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //创建跨应用会话
                Conversation con = Conversation.createSingleConversation("114060100206", "748b4c279d7d89648adefba1");
                MessageContent content = new TextContent("3");
//创建一条消息
                Message message = con.createSendMessage(content);
//发送消息
                JMessageClient.sendMessage(message);
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
        JMessageClient.logout();
    }
}

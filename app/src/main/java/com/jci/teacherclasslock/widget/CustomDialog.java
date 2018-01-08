package com.jci.teacherclasslock.widget;

/**
 * 创建人: 谭火朋
 * 创建时间: 2017/11/10 0010 20:37
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jci.teacherclasslock.R;

/**
 * 自定义Dialog
 */
public class CustomDialog extends Dialog {
    private String title;
    private String message;
    private View.OnClickListener onNegateClickListener;
    private View.OnClickListener onPositiveClickListener;
    private String state;//0、警告;1、成功;2、失败;
    private int topShow;//0、隐藏1、显示

    public CustomDialog(Context context) {
        super(context);
    }

    /**
     * @param context 上下文
     * @param theme   给dialog设置的主题
     */
    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.custom_dialog);
        //设置dialog的大小
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth() - 100; //设置dialog的宽度为当前手机屏幕的宽度-100
        getWindow().setAttributes(p);


        LinearLayout llTop = (LinearLayout) findViewById(R.id.callback_dialog_ll_top);
        llTop.setVisibility(topShow);
        ImageView stateImg = (ImageView) findViewById(R.id.callback_dialog_img_state);
        if (!TextUtils.isEmpty(state)) {
            stateImg.setVisibility(View.VISIBLE);
            stateImg.setImageResource(state.equals("0") ? R.mipmap.smiley_00 : state.equals("1") ? R.mipmap.ic_success : R.mipmap.smiley_00);
        } else {
            stateImg.setVisibility(View.GONE);
        }
        TextView textTitle = (TextView) findViewById(R.id.callback_dialog_tv_title);
        if (!TextUtils.isEmpty(title)) {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText(title);
        } else {
            textTitle.setVisibility(View.GONE);
        }
        TextView textMsg = (TextView) findViewById(R.id.callback_dialog_tv_msg);
        if (!TextUtils.isEmpty(message)) {
            textMsg.setVisibility(View.VISIBLE);
            textMsg.setText(message);
        }
        TextView divider = (TextView) findViewById(R.id.callback_dialog_tv_dividers);
        TextView negate = (TextView) findViewById(R.id.callback_dialog_tv_negate);
        if (onNegateClickListener != null) {
            negate.setVisibility(View.VISIBLE);
            negate.setOnClickListener(onNegateClickListener);
        } else {
            divider.setVisibility(View.GONE);
            negate.setVisibility(View.GONE);
        }
        TextView positive = (TextView) findViewById(R.id.callback_dialog_tv_positive);
        if (onPositiveClickListener != null) {
            positive.setVisibility(View.VISIBLE);
            positive.setOnClickListener(onPositiveClickListener);
        } else {
            divider.setVisibility(View.GONE);
            positive.setVisibility(View.GONE);
        }

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMsg(String message) {
        this.message = message;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTopShow(int topShow) {
        this.topShow = topShow;
    }

    /**
     * 确定按钮
     */
    public void setOnPositiveListener(View.OnClickListener onPositiveClickListener) {
        this.onPositiveClickListener = onPositiveClickListener;
    }

    /**
     * 取消按钮
     */
    public void setOnNegateListener(View.OnClickListener onNegateClickListener) {
        this.onNegateClickListener = onNegateClickListener;
    }
}


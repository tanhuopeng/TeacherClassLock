package com.jci.teacherclasslock.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.ihidea.multilinechooselib.MultiLineChooseLayout;
import com.jci.teacherclasslock.R;
import com.jci.teacherclasslock.core.ConstantValues;
import com.jci.teacherclasslock.model.CourseInfo;
import com.jci.teacherclasslock.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.jci.teacherclasslock.R.id.toolbar;
import static com.jci.teacherclasslock.R.id.toolbar_title;

public class EditCourseActivity extends AppCompatActivity {
    
    private EditText edt_classname;
    private EditText edt_classroom;
    private EditText edt_teacher;
    private TextView txt_section;
    private TextView txt_week;
    private TextView txt_time;
    private TextView txt_day;
    private Button btn_finish;
    private LinearLayout btn_section;
    private LinearLayout btn_week;
    private LinearLayout btn_time;
    private LinearLayout btn_day;
    private TextView mTitle;
    private Toolbar mToolbar;
    private TimePickerView pvTime;
    private String time;
    private LayoutInflater inflater;
    private List<String> mEquipData = new ArrayList<>();
    private MultiLineChooseLayout multiChoose;
    List<String> multiChooseResult = new ArrayList<>();
    private String[] week = {"周一","周二","周三","周四","周五","周六","周日"};
    private String[] selection = {"1-2节","3-4节","5-6节","7-8节","9-10节","11-12节"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        initWidget();
        initData();
        iniEvent();

    }

    private void initData() {
        mTitle.setText("编辑课程");
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                time = getTime(date);
                txt_time.setText(time);
            }
        });
        loadDialogData();
    }

    private void loadDialogData() {
        for (int i = 1; i <= 25; i++){
            mEquipData.add(i+"");
        }
    }

    private void showDialog() {
        View dialog;
        dialog = inflater.inflate(R.layout.widget_week_dialog,null);
        multiChoose = (MultiLineChooseLayout) dialog.findViewById(R.id.flowLayout);
        multiChoose.setList(mEquipData);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择周次");
        builder.setView(dialog);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                multiChooseResult = multiChoose.getAllItemSelectedTextWithListArray();
                if (multiChooseResult != null && multiChooseResult.size() > 0) {
                    String textSelect = "";
                    for (int i = 0; i < multiChooseResult.size(); i++) {
                        if (i == multiChooseResult.size() -1){
                            textSelect += multiChooseResult.get(i);
                        }else {
                            textSelect += multiChooseResult.get(i) + " , ";
                        }
                    }
                    txt_week.setText(textSelect + "周");
                }
            }
        });
        builder.show();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    private void iniEvent() {
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        btn_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog("选择日期",week,txt_day);

            }
        });
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btn_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog("选择节次",selection, txt_section);

            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clssname = edt_classname.getText().toString();
                String classroom = edt_classroom.getText().toString();
                String teacher = edt_teacher.getText().toString();
                if (TextUtils.isEmpty(clssname)){
                    ToastUtils.showShort("未输入课程名");
                    return;
                }
                if (TextUtils.isEmpty(classroom)){
                    ToastUtils.showShort("未输入教室名");
                    return;
                }
                if (TextUtils.isEmpty(teacher)){
                    ToastUtils.showShort("未输入教师名");
                    return;
                }
                String section = txt_section.getText().toString();
                String time = txt_time.getText().toString();
                String day = txt_day.getText().toString();
                String week = txt_week.getText().toString();
                CourseInfo info = new CourseInfo();
                info.setWeek(week);
                info.setTime(time);
                info.setDay(day);
                info.setStart(section);
                info.setName(clssname);
                info.setRoom(classroom);
                info.setTeacher(teacher);
                info.setTerm(ConstantValues.term);
                info.setClasses(ConstantValues.classes);
                Log.e("aaaaaaaaaaa",ConstantValues.classes);
                info.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            ToastUtils.showShort("提交成功");
                            edt_classname.setText("");
                            edt_classroom.setText("");
                            edt_teacher.setText("");
                        }else {
                            ToastUtils.showShort("提交失败");
                        }
                    }
                });


            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWidget() {
        edt_classname = (EditText)findViewById(R.id.edt_classname);
        edt_classroom = (EditText)findViewById(R.id.edt_classroom);
        edt_teacher = (EditText)findViewById(R.id.edt_teacher);
        txt_section = (TextView) findViewById(R.id.txt_section);
        txt_week = (TextView)findViewById(R.id.txt_week);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_day = (TextView) findViewById(R.id.txt_day);
        btn_finish = (Button)findViewById(R.id.btn_finish);
        btn_section = (LinearLayout)findViewById(R.id.btn_section);
        btn_week = (LinearLayout)findViewById(R.id.btn_week);
        btn_time = (LinearLayout) findViewById(R.id.btn_time);
        btn_day = (LinearLayout) findViewById(R.id.btn_day);
        mTitle = (TextView) this.findViewById(toolbar_title);
        inflater = LayoutInflater.from(this);
        mToolbar = (Toolbar) this.findViewById(toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_toolbar_back);
    }

    private void showSingleChoiceDialog(String title,final String[] str1, final TextView text) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(str1, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = str1[which];
                text.setText(str);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

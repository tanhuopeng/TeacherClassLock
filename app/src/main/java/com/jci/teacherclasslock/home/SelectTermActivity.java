package com.jci.teacherclasslock.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jci.teacherclasslock.R;
import com.jci.teacherclasslock.core.ConstantValues;

import static com.jci.teacherclasslock.R.id.toolbar;
import static com.jci.teacherclasslock.R.id.toolbar_title;

public class SelectTermActivity extends AppCompatActivity {

    private TextView mTitle;
    private Toolbar mToolbar;
    private RadioGroup mRadioGroup;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_term);
        mTitle = (TextView) this.findViewById(toolbar_title);
        mTitle.setText("选择学期");
        mBtn = (Button) this.findViewById(R.id.mSelectTerm_Btn);
        mToolbar = (Toolbar) this.findViewById(toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_toolbar_back);
        mRadioGroup = (RadioGroup) this.findViewById(R.id.mSecletTermActivity_RadioGrop);
        initEvent();
    }

    private void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton)findViewById(mRadioGroup.getCheckedRadioButtonId());
                if (radioButton.getText().toString() != null){
                    ConstantValues.term = radioButton.getText().toString();
                    Intent intnt = new Intent(SelectTermActivity.this, EditCourseActivity.class);
                    startActivity(intnt);
                    finish();
                }
            }
        });
    }
}

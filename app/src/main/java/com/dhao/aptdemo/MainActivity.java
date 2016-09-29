package com.dhao.aptdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dhao.ioc_api.Ioc;
import com.dhao.ioc_api.ViewInject;
import com.example.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_ioc)
    TextView tvIoc;
    @BindView(R.id.tv_text1)
    TextView tvText1;
    @BindView(R.id.tv_text2)
    TextView tvText2;
    @BindView(R.id.button)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ioc.inject(this);
//        ViewInject viewInject=new MainActivity$$ViewInject();
//        viewInject.inject(this,this);
        tvIoc.setText("行不行啊？");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvText1.setText("啊啊啊啊");
                tvText2.setText("呀呀呀呀");
            }
        });
    }
}

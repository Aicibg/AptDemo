package com.dhao.aptdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dhao.ioc_api.Ioc;
import com.example.BindView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_ioc)
    TextView tvIoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ioc.inject(this);
//        tvIoc.setText("行不行啊？");
    }
}
